package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import kotlin.math.PI

@Autonomous
@Config
class SampleAuto: LinearOpMode()
{
	companion object
	{
		@JvmField
		var transferDelay = 1.0;
		@JvmField
		var intakeDelay = 1.0;
		@JvmField
		var outtakeDelay = 1.0;
		@JvmField
		var slideScore = -900;
	}

	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"));
		val outtake = Outtake(hardwareMap);
		val intake = Intake(hardwareMap.crservo.get("rotator0"), null, null);
		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		val arm = Arm(hardwareMap.servo.get("arm"));


		val preload = drive.trajectorySequenceBuilder(Pose2d(-6.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(Vector2d(-6.0, -28.0)).build();
		val samp1 = drive.trajectorySequenceBuilder(preload.end())
			.lineToLinearHeading(Pose2d(-30.0, -40.0, PI))
			.lineToLinearHeading(Pose2d(-36.0, -21.5, PI))
      .build();
		val toScore = drive.trajectorySequenceBuilder(samp1.end())
			.lineToLinearHeading(Pose2d(-53.5, -52.0, Math.toRadians(45.0)),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
      .build();
		val samp2 = drive.trajectorySequenceBuilder(toScore.end())
			.lineToLinearHeading(Pose2d(-44.0, -21.5, PI))
      .build();
		val twoScore = drive.trajectorySequenceBuilder(samp2.end())
			.lineToLinearHeading(Pose2d(-53.5, -52.0, Math.toRadians(45.0)),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
      .build();
		val park = drive.trajectorySequenceBuilder(samp2.end())
			.lineToLinearHeading(Pose2d(-27.0, -10.0, Math.toRadians(90.0)))
      .build();

		drive.poseEstimate = preload.start();

    val startHeading = drive.poseEstimate.heading;

		outtake.armDown();
		outtake.bucketDown();
		claw.close();
		arm.down();
		hslide.zero();

		waitForStart();

		slide.gotoPos(-1400);
		drive.followTrajectorySequence(preload);

		slide.gotoPos(-900);
		while(slide.busy());
		claw.open();
		slide.runToPosition(0);

		drive.followTrajectorySequence(samp1);

		intake.forward();
		hslide.gotoPos(0.65);

		delay(intakeDelay);

		hslide.score();
		arm.up();

		delay(transferDelay);
		intake.reverse();
		delay(transferDelay);

		intake.stop();
		arm.down();
		hslide.gotoPos(1.0);
		slide.gotoPos(-2500);

		drive.followTrajectorySequence(toScore);

		//Arm position to rotate to the opposite side of the bot (sample scoring pos)
		outtake.arm.position = 0.6;

		delay(outtakeDelay);

		outtake.armDown();
		delay(0.25);
		slide.lower();

		drive.followTrajectorySequence(samp2);

		intake.forward();
		hslide.gotoPos(0.65);

		delay(intakeDelay);

		hslide.score();
		arm.up();

		delay(transferDelay);
		intake.reverse();
		delay(transferDelay);

		intake.stop();
		arm.down();
		hslide.zero();
		slide.gotoPos(-2500);

    //Different starting point (samp2 instead of samp1)
		drive.followTrajectorySequence(twoScore);

		//Arm position to rotate to the opposite side of the bot (sample scoring pos)
		outtake.arm.position = 0.6;

		delay(outtakeDelay);

		//Arm position to stop the bot from clipping anything
		outtake.armDown();
		delay(0.25);
		slide.lower();

		drive.followTrajectorySequence(park);

		rotPos = drive.localizer.poseEstimate.heading + startHeading;
	}

	fun delay(time: Double)
	{
		val elapsedTime = ElapsedTime();
		elapsedTime.reset();
		while(elapsedTime.seconds() < time);
	}
}
