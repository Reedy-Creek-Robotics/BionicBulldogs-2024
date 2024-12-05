package org.firstinspires.ftc.teamcode.opmode.auto

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.modules.ui.*
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence

@TeleOp
class CollectionTest: LinearOpMode()
{
	private var delay1 = FloatPtr(1.0f);
	private var delay2 = FloatPtr(1.0f);

	private lateinit var paths: ArrayList<TrajectorySequence>;
	private lateinit var drive: SampleMecanumDrive;
	private lateinit var specimenOuttake: SpecimenOuttake;
	private lateinit var sampleOuttake: SampleOuttake;
	private lateinit var arm: Arm;
	private lateinit var intake: Intake;
	private lateinit var hSlide: HSlide;

	fun run()
	{
		var ind = 0;
		drive.poseEstimate = paths[0].start();

		for(i in 0 .. 3)
		{
			hSlide.gotoPos(0.5);
			intake.forward();

			val elapsedTime = ElapsedTime();
			elapsedTime.reset();
			while(elapsedTime.seconds() < delay1.value);

			intake.stop();
			hSlide.gotoPos(hSlide.min());
			drive.followTrajectorySequence(paths[ind++]);
			intake.reverse();

			elapsedTime.reset();
			while(elapsedTime.seconds() < delay2.value);

			intake.stop();
			if(i < 2)
			{
				drive.followTrajectorySequence(paths[ind++]);
			}
		}
	}

	override fun runOpMode()
	{
		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		specimenOuttake = SpecimenOuttake(claw, slide);

		val outtake = Outtake(hardwareMap);
		sampleOuttake = SampleOuttake(slide, outtake);

		arm = Arm(hardwareMap.servo.get("arm"));
		intake = Intake(
			hardwareMap.crservo.get("rotator0"), null, null
		);
		hSlide = HSlide(hardwareMap.servo.get("hslide"));

		val drive = SampleMecanumDrive(hardwareMap);

		paths = ArrayList();

		var builder = drive.trajectorySequenceBuilder(pos(sampleX[0], sampleY, 90));

		builder.lineToLinearHeading(pos(sampleX[0], specimenDropoffY, 180));
		paths.add(builder.build());

		for(i in 1 .. 2)
		{
			builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
			builder.lineToLinearHeading(pos(sampleX[i], sampleY, 90));
			paths.add(builder.build());

			builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
			builder.lineToLinearHeading(pos(sampleX[i], specimenDropoffY, 180));
			paths.add(builder.build());
		}

		val ui = UI();
		ui.init(telemetry, gamepad1);

		waitForStart();

		while(opModeIsActive())
		{
			ui.floatInput("delay 1", delay1, 0.05f);
			ui.floatInput("delay 2", delay2, 0.05f);
			if(ui.button("run"))
			{
				run();
			}
			ui.update();
		}
	}
}