package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.Vec2
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.modules.ui.FloatPtr
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

fun getSamplePosition(sample: LLResultTypes.ColorResult): Vec2
{
	val tx = sample.targetXDegrees;
	val ty = sample.targetYDegrees;

	val a1 = -25 * PI / 180;
	val a2 = ty * PI / 180;
	val dy = -13.75 / tan(a1 + a2);

	val a3 = 0 * PI / 180;
	val a4 = tx * PI / 180;
	val dx = dy * tan(a3 + a4);
	return Vec2(dx.toFloat(), dy.toFloat());
}

@TeleOp
class LimeLight: LinearOpMode()
{
	private fun processSampleList(list: List<LLResultTypes.ColorResult>, sample: LLResultTypes.ColorResult, closestDist: FloatPtr)
	{
		val pos = getSamplePosition(sample);
		for(sample2 in list)
		{
			val pos2 = getSamplePosition(sample2);
			val dist2 = sqrt((pos2.x - pos.x).pow(2) + (pos2.y - pos.y).pow(2));
			telemetry.addLine("sample2 x: ${pos2.x}, y: ${pos2.y}, d: $dist2");
			if(sample == sample2)
			{
				telemetry.addLine("sample same as target, skipping");
				continue;
			}
			if(pos2.y > pos.y)
			{
				telemetry.addLine("sample behind target, skipping");
				continue;
			}
			if(pos2.x > pos.x + 5)
			{
				telemetry.addLine("sample too far right, skipping");
				continue;
			}
			if(pos2.x < pos.x - 5)
			{
				telemetry.addLine("sample too far left, skipping");
				continue;
			}
			if(closestDist.value > dist2)
				closestDist.value = dist2;
		}
	}

	override fun runOpMode()
	{
		val drive = MecanumDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0));
		val limelight = hardwareMap.get(Limelight3A::class.java, "limelight");

		val hslide = HSlide(hardwareMap);
		val arm = Arm(hardwareMap);
		val intake = Intake(hardwareMap);

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

				var closestDist = 0.0f;
				var closestSample: LLResultTypes.ColorResult? = null;
				for(sample: LLResultTypes.ColorResult in targetResult)
				{
					val pos = getSamplePosition(sample);
					telemetry.addLine("--- sample x: ${pos.x}, y: ${pos.y} ---");
					if(sample.targetYDegrees < -20)
					{
						telemetry.addLine("sample too low, skipping");
						continue;
					}
					val dist = FloatPtr(9999.0f);

					telemetry.addLine("--- Red List ---");
					processSampleList(otherColor, sample, dist);
					telemetry.addLine("--- Blue List ---");
					processSampleList(otherColor2, sample, dist);
					telemetry.addLine("--- Yellow List ---");
					processSampleList(targetResult, sample, dist);

					if(dist.value > closestDist)
					{
						closestSample = sample;
						closestDist = dist.value;
					}
				}
				if(closestSample == null)
				{
					telemetry.addLine("--- No Closest Sample Found ---");
					telemetry.update();
				}
				else
				{
					val pos = getSamplePosition(closestSample);
					telemetry.addLine("--- Closest Sample ---");
					telemetry.addLine("dist: $closestDist");
					telemetry.addLine("x: ${pos.x}");
					telemetry.addLine("y: ${pos.y}");
					telemetry.addLine("tx: ${closestSample.targetXDegrees}");
					telemetry.addLine("ty: ${closestSample.targetYDegrees}");
					telemetry.update();
					dx = pos.x.toDouble();
					dy = pos.y.toDouble();
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

				val hslidePos = (HSlide.min - HSlide.max) * ((dy - 5 - 9.5) / 21.0) + HSlide.max;
				arm.down();
				hslide.gotoPos(hslidePos);

				telemetry.clearAll();
				telemetry.addLine("--- Intaking ---");
				telemetry.addLine("dx: $dx");
				telemetry.addLine("dy: $dy");
				telemetry.addLine("tx: $tx");
				telemetry.addLine("ty: ${0}");
				telemetry.addLine("hslidePos: $hslidePos");
				telemetry.update();

				intake.forward();
				val e = ElapsedTime();
				e.reset();
				while(e.seconds() < 2);
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