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
import org.firstinspires.ftc.teamcode.modules.robot.SampleOuttake
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
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
		var transferDelay = 0.5;
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
		val slide = Slide(hardwareMap);
		val sampleOuttake = SampleOuttake(slide, outtake);
		val intake = Intake(hardwareMap);
		val claw = SpeciminClaw(hardwareMap);
		val arm = Arm(hardwareMap.servo.get("arm"));
    val specimenClaw = SpecimenOuttake(claw, slide);

		val preload = drive.trajectorySequenceBuilder(Pose2d(-30.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(Vector2d(-6.0, -28.0)).build();
		val samp1 = drive.trajectorySequenceBuilder(preload.end())
			.lineToLinearHeading(Pose2d(-30.0, -40.0, PI))
			.lineToLinearHeading(Pose2d(-36.0, -22.0, PI),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
      .build();
		val toScore = drive.trajectorySequenceBuilder(samp1.end())
			.lineToLinearHeading(Pose2d(-51.5, -51.5, Math.toRadians(45.0)),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
      .build();
		val samp2 = drive.trajectorySequenceBuilder(toScore.end())
			.lineToLinearHeading(Pose2d(-44.0, -22.0, PI),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
      .build();
		val twoScore = drive.trajectorySequenceBuilder(samp2.end())
			.lineToLinearHeading(Pose2d(-51.5, -51.5, Math.toRadians(45.0)),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
      .build();
		val park = drive.trajectorySequenceBuilder(samp2.end())
			.lineToLinearHeading(Pose2d(-27.0, -10.0, Math.toRadians(90.0)))
      .build();

		drive.poseEstimate = preload.start();

    val startHeading = drive.poseEstimate.heading;

    specimenClaw.init();

		outtake.armDown();
		outtake.bucketDown();
		claw.close();
		arm.down();
		hslide.zero();

    sampleOuttake.init();

    intake.zeroRotator();

		waitForStart();

    specimenClaw.collect();
    specimenClaw.waitUntilIdle();

		drive.followTrajectorySequence(preload);

    specimenClaw.score();
    specimenClaw.waitUntilIdle();

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
    sampleOuttake.up();

		drive.followTrajectorySequenceAsync(toScore);

    while(drive.isBusy())
    {
      drive.update();
      sampleOuttake.update();
    }

    sampleOuttake.score();
    sampleOuttake.waitUntilIdle();

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

    sampleOuttake.up();

    //Different starting point (samp2 instead of samp1)

		//Arm position to rotate to the opposite side of the bot (sample scoring pos)
		drive.followTrajectorySequenceAsync(twoScore);

    while(drive.isBusy())
    {
      drive.update();
      sampleOuttake.update();
    }

		//Arm position to stop the bot from clipping anything
    sampleOuttake.score();
    sampleOuttake.waitUntilIdle();

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
