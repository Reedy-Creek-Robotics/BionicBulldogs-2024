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
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants
import kotlin.math.PI

@Autonomous
@Config
class SampleAutoV3: LinearOpMode()
{
	companion object
	{
		@JvmField
		var transferDelay = 0.7;
		@JvmField
		var intakeDelay = 1.0;
		@JvmField
		var outtakeDelay = 1.0;
		@JvmField
		var slideScore = -900;
	}

  val startX = -42.0;
  val startY = -64.5;

  val scoreX = -57.5;
  val scoreY = -57.5;

  val sampleX = arrayOf(-38.5, -46.0, -56.0);
  val sampleY = -30.0;

	fun rotation(angle: Int): Double
	{
		return Math.toRadians(-(angle.toDouble() - 90))
	}

	fun pos(x: Double, y: Double, angle: Int): Pose2d
	{
		return Pose2d(x, y, rotation(angle));
	}

	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"));
		val outtake = Outtake(hardwareMap);
		val slide = Slide(hardwareMap);
		val sampleOuttake = SampleOuttake(slide, outtake);
		val intake = Intake(hardwareMap.crservo.get("rotator0"), null, null);
		val claw = SpeciminClaw(hardwareMap);
		val arm = Arm(hardwareMap.servo.get("arm"));
    val specimenClaw = SpecimenOuttake(claw, slide);

		val preload = drive.trajectorySequenceBuilder(Pose2d(startX, startY, rotation(90)))
			.lineToLinearHeading(pos(scoreX, scoreY, 45))
      .build();

		val samp1 = drive.trajectorySequenceBuilder(preload.end())
			.lineToLinearHeading(pos(sampleX[0], sampleY, -96))
      .build();

		val toScore = drive.trajectorySequenceBuilder(samp1.end())
			.lineToLinearHeading(pos(scoreX, scoreY, 45))
      .build();

		val samp2 = drive.trajectorySequenceBuilder(toScore.end())
			.lineToLinearHeading(pos(sampleX[1], sampleY - 3, -96),
        velOverride(maxVel = DriveConstants.MAX_VEL * 0.8),
        accelOverride(maxAccel = 30.0))
      .build();
		val twoScore = drive.trajectorySequenceBuilder(samp2.end())
			.lineToLinearHeading(pos(scoreX, scoreY, 45),
        velOverride(maxVel = DriveConstants.MAX_VEL * 0.8),
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
		claw.open();
		arm.down();
		hslide.zero();

    sampleOuttake.init();

		waitForStart();

    sampleOuttake.up();
		drive.followTrajectorySequenceAsync(preload);

    while(drive.isBusy())
    {
      drive.update();
      sampleOuttake.update();
    }

    sampleOuttake.score();
    sampleOuttake.waitUntilIdle();

		drive.followTrajectorySequence(samp1);

		intake.forward();
		hslide.gotoPos(0.4);

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
		hslide.gotoPos(0.4);

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

		// drive.followTrajectorySequence(park);

		rotPos = drive.localizer.poseEstimate.heading + startHeading;
	}

	fun delay(time: Double)
	{
		val elapsedTime = ElapsedTime();
		elapsedTime.reset();
		while(elapsedTime.seconds() < time);
	}
}
