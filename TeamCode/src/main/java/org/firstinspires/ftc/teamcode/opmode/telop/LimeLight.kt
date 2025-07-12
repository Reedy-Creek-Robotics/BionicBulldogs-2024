package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.roadrunner.*
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.Vec2
import org.firstinspires.ftc.teamcode.modules.actions.FunctionAction
import org.firstinspires.ftc.teamcode.modules.actions.WaitForOtherAction
import org.firstinspires.ftc.teamcode.modules.fmt
import org.firstinspires.ftc.teamcode.modules.format
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.lerp
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.modules.ui.FloatPtr
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.*

fun getSamplePosition(sample: LLResultTypes.ColorResult): Vec2
{
	return getSamplePosition(sample.targetXDegrees.toFloat(), sample.targetYDegrees.toFloat());
}

fun getSamplePosition(tx: Float, ty: Float): Vec2
{
	val a1 = -25 * PI / 180;
	val a2 = ty * PI / 180;
	val dy = -13.75 / tan(a1 + a2);

	val a3 = 0 * PI / 180;
	val a4 = tx * PI / 180;
	val dx = dy * tan(a3 + a4);
	return Vec2(dx.toFloat(), dy.toFloat());
}

class Sample
{
	lateinit var res: LLResultTypes.ColorResult;
	lateinit var pos: Vec2;
	var dist = 0.0f;
	var width = 0.0f;
	var height = 0.0f;
}

@TeleOp
class LimeLight: LinearOpMode()
{
	private fun getSampleSize(sample: LLResultTypes.ColorResult): Vec2
	{
		val corners = sample.targetCorners;
		val aabb1 = Vec2();
		val aabb2 = Vec2();

		aabb1.x = corners[0][0].toFloat();
		aabb1.y = corners[0][1].toFloat();
		aabb2.x = corners[0][0].toFloat();
		aabb2.y = corners[0][1].toFloat();

		for(corner in corners)
		{
			aabb1.x = min(aabb1.x, corner[0].toFloat());
			aabb1.y = min(aabb1.y, corner[1].toFloat());
			aabb2.x = max(aabb2.x, corner[0].toFloat());
			aabb2.y = max(aabb2.y, corner[1].toFloat());
		}
		telemetry.addLine("aabb1x: ${aabb1.x}");
		telemetry.addLine("aabb1y: ${aabb1.y}");
		telemetry.addLine("aabb2x: ${aabb2.x}");
		telemetry.addLine("aabb2y: ${aabb2.y}");

		//size: 960x720

		var x1Raw = aabb1.x / 960.0f;
		var y1Raw = aabb1.y / 720.0f;
		var x2Raw = aabb2.x / 960.0f;
		var y2Raw = aabb2.y / 720.0f;

		telemetry.addLine("x1Raw: $x1Raw");
		telemetry.addLine("y1Raw: $y1Raw");
		telemetry.addLine("x2Raw: $x2Raw");
		telemetry.addLine("y2Raw: $y2Raw");

		//fov 54.5x42
		x1Raw = lerp(-54.5f / 2.0f, 54.5f / 2.0f, x1Raw);
		x2Raw = lerp(-54.5f / 2.0f, 54.5f / 2.0f, x2Raw);
		y1Raw = lerp(-42.0f / 2.0f, 42.0f / 2.0f, y1Raw);
		y2Raw = lerp(-42.0f / 2.0f, 42.0f / 2.0f, y2Raw);

		telemetry.addLine("x1Raw: $x1Raw");
		telemetry.addLine("y1Raw: $y1Raw");
		telemetry.addLine("x2Raw: $x2Raw");
		telemetry.addLine("y2Raw: $y2Raw");

		val topLeft = getSamplePosition(x1Raw, y1Raw);
		val bottomRight = getSamplePosition(x2Raw, y2Raw);

		telemetry.addLine("c1: ${topLeft.x}");
		telemetry.addLine("c1: ${topLeft.y}");
		telemetry.addLine("c2: ${bottomRight.x}");
		telemetry.addLine("c2: ${bottomRight.y}");

		val width = bottomRight.x - topLeft.x;
		val height = bottomRight.y - topLeft.y;

		return Vec2(width, height);
	}

	private fun processSampleList(
		list: List<LLResultTypes.ColorResult>,
		sample: Sample,
		closestDist: FloatPtr
	)
	{
		for(sample2 in list)
		{
			val pos = getSamplePosition(sample2);
			val dist = sqrt((pos.x - sample.pos.x).pow(2) + (pos.y - sample.pos.y).pow(2));
			if(sample.res == sample2)
				continue;
			if(pos.y > sample.pos.y)
				continue;
			if(pos.x > sample.pos.x + 3)
				continue;
			if(pos.x < sample.pos.x - 3)
				continue;
			if(closestDist.value > dist)
				closestDist.value = dist;
		}
	}

	override fun runOpMode()
	{
		val drive = MecanumDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0));
		val limelight = hardwareMap.get(Limelight3A::class.java, "limelight");

		val hslide = HSlide(hardwareMap);
		val arm = Arm(hardwareMap);
		val intake = Intake(hardwareMap);

		val colorSensor = ColorSensor(hardwareMap);

		arm.up();
		hslide.zero();

		limelight.setPollRateHz(100);
		limelight.start();
		limelight.pipelineSwitch(0);

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		var dx = 0.0;
		var dy = 0.0;

		while(opModeIsActive())
		{
			gamepad.copy();
			if(gamepad.cross())
			{
				var targetResult: List<LLResultTypes.ColorResult>?;
				var otherColor: List<LLResultTypes.ColorResult>?;
				var otherColor2: List<LLResultTypes.ColorResult>?;

				limelight.pipelineSwitch(0);
				var result = limelight.latestResult;
				while(result == null || !result.isValid || result.pipelineIndex != 0)
					result = limelight.latestResult;
				targetResult = result.colorResults;
				telemetry.clearAll();
				telemetry.addLine("--- Found Yellow ---");
				telemetry.update();

				limelight.pipelineSwitch(2);
				result = limelight.latestResult;
				while(result == null || !result.isValid || result.pipelineIndex != 2)
					result = limelight.latestResult;
				otherColor = result.colorResults;
				telemetry.clearAll();
				telemetry.addLine("--- Found Yellow ---");
				telemetry.addLine("--- Found Red ---");
				telemetry.update();

				limelight.pipelineSwitch(1);
				result = limelight.latestResult;
				while(result == null || !result.isValid || result.pipelineIndex != 1)
					result = limelight.latestResult;
				otherColor2 = result.colorResults;
				telemetry.clearAll();
				telemetry.addLine("--- Found Yellow ---");
				telemetry.addLine("--- Found Red ---");
				telemetry.addLine("--- Found Blue ---");
				telemetry.update();

				telemetry.addLine("Red ${otherColor.size}");
				telemetry.addLine("Blue ${otherColor2.size}");
				telemetry.addLine("Yellow ${targetResult.size}");

				val samples = ArrayList<Sample>();
				for(res: LLResultTypes.ColorResult in targetResult)
				{
					val pos = getSamplePosition(res);
					telemetry.addLine("--- sample x: ${pos.x}, y: ${pos.y} ---");
					val sample = Sample();
					sample.res = res;
					sample.pos = pos;
					val dist = FloatPtr(9999.0f);

					processSampleList(otherColor, sample, dist);
					processSampleList(otherColor2, sample, dist);
					processSampleList(targetResult, sample, dist);

					if(sample.pos.y > 32)
					{
						telemetry.addLine("sample too far forward, skipping");
						continue;
					}

					if(dist.value < 3)
					{
						telemetry.addLine("sample too close to other samples, skipping");
						continue;
					}

					sample.dist = dist.value;
					samples.add(sample);
				}

				samples.sortWith({a, b -> if(a.pos.y > b.pos.y) -1 else 1});

				for(i in 0 until samples.size)
				{
					val sample = samples[i];
					//telemetry.fmt("sample %d : x: %.2f, y: %.2f, dist: %.2f", i, sample.pos.x, sample.pos.y, sample.dist);
					telemetry.fmt("sample $i : x: ${sample.pos.x.format(2)}, y: ${sample.pos.y.format(2)}, dist: ${sample.dist.format(2)}");
				}

				//val len = min(samples.size, 5);

				//var maxY = 999999.0f;
				//var maxSample: Sample? = null;

				//for(i in 0 until len)
				//{
				//	val sample = samples[i];
				//	if(sample.pos.y < maxY)
				//	{
				//		maxY = sample.pos.y;
				//		maxSample = sample;
				//	}
				//}
				val maxSample = samples[0];

				if(maxSample == null)
				{
					telemetry.addLine("--- No Closest Sample Found ---");
					telemetry.update();
				}
				else
				{
					telemetry.addLine("--- Closest Sample ---");
					telemetry.addLine("dist: ${maxSample.dist}");
					telemetry.addLine("x: ${maxSample.pos.x}");
					telemetry.addLine("y: ${maxSample.pos.y}");
					telemetry.addLine("tx: ${maxSample.res.targetXDegrees}");
					telemetry.addLine("ty: ${maxSample.res.targetYDegrees}");
					dx = maxSample.pos.x.toDouble();
					dy = maxSample.pos.y.toDouble();
					telemetry.update();
				}
			}

			if(gamepad.circle())
			{
				val tx = -dx - 5.5;
				telemetry.clearAll();
				telemetry.addLine("--- Moving ---");
				telemetry.addLine("dx: $dx");
				telemetry.addLine("dy: $dy");
				telemetry.addLine("tx: $tx");
				telemetry.addLine("ty: ${0}");
				telemetry.update();

				val tangent = Math.toRadians(if(dx > 0) -90.0 else 90.0);
				val trajectory = drive.actionBuilder(Pose2d(0.0, 0.0, 0.0))
					.setTangent(tangent)
					.splineToConstantHeading(Vector2d(9.5, tx), tangent)
					.build();

				runBlocking(trajectory);

				telemetry.clearAll();
				telemetry.addLine("--- Intaking ---");
				telemetry.addLine("dx: $dx");
				telemetry.addLine("dy: $dy");
				telemetry.addLine("tx: $tx");
				telemetry.addLine("ty: ${0}");
				telemetry.update();

				val hslidePos = (HSlide.min - HSlide.max) * ((dy - 5 - 9.5) / 21.0) + HSlide.max;
				val hslidePos2 = (HSlide.min - HSlide.max) * ((dy - 8 - 9.5) / 21.0) + HSlide.max;
				hslide.gotoPos(hslidePos2);

				val e = ElapsedTime();
				e.reset();
				while(e.seconds() < 1);

				arm.down();
				e.reset();
				while(e.seconds() < 1);

				intake.forward();
				hslide.gotoPos(hslidePos);

				var outtakeTime = 0.0f;

				val action = WaitForOtherAction(
					FunctionAction(
						fun(elapsedTime: Float): Boolean
						{
							colorSensor.update();
							when(intake.state)
							{
								Intake.State.Reverse ->
								{
									if(outtakeTime == 0.0f && colorSensor.col == ColorSensor.NONE)
										outtakeTime = elapsedTime;
									else if(outtakeTime < elapsedTime + 0.5)
									{
										intake.forward();
										outtakeTime = 0.0f;
									}
								}

								Intake.State.Forward ->
								{
									if(colorSensor.col == ColorSensor.RED || colorSensor.col == ColorSensor.BLUE)
									{
										intake.reverse();
										outtakeTime = elapsedTime;
									}
								}

								Intake.State.Stop    ->
								{
								}
							}
							if(colorSensor.col == ColorSensor.YELLOW)
								return false;
							return true;
						}
					),
					SequentialAction(
						SleepAction(2.0),
						drive.actionBuilder(Pose2d(9.5, tx, 0.0))
							.setTangent(Math.toRadians(90.0))
							.splineToConstantHeading(Vector2d(9.5, tx + 3), Math.toRadians(90.0))
							.build(),
						drive.actionBuilder(Pose2d(9.5, tx + 3, 0.0))
							.setTangent(Math.toRadians(-90.0))
							.splineToConstantHeading(Vector2d(9.5, tx - 3), Math.toRadians(-90.0))
							.build(),
						FunctionAction(
							fun(elapsedTime: Float): Boolean
							{
								val hslidePos3 = (HSlide.min - HSlide.max) * ((dy - 2 - 9.5) / 21.0) + HSlide.max;
								hslide.gotoPos(hslidePos3);
								return false;
							}
						),
						drive.actionBuilder(Pose2d(9.5, tx - 3, 0.0))
							.setTangent(Math.toRadians(90.0))
							.splineToConstantHeading(Vector2d(9.5, tx), Math.toRadians(90.0))
							.build()
					)
				);

				runBlocking(action);
				drive.setDrivePowers(PoseVelocity2d(Vector2d(0.0, 0.0), 0.0));
				intake.stop();

				telemetry.clearAll();
				telemetry.addLine("--- Done ---");
				telemetry.addLine("dx: $dx");
				telemetry.addLine("dy: $dy");
				telemetry.addLine("tx: $tx");
				telemetry.addLine("ty: ${0}");
				telemetry.addLine("hslidePos: $hslidePos");
				telemetry.update();
			}
		}
	}
}