package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

@Autonomous
class SpecimenAuto: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);

		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		val specimenOuttake = SpecimenOuttake(claw, slide);

		val path = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(
				Vector2d(0.0, -28.0)
			).build();

		val path2 = drive.trajectorySequenceBuilder(path.end()).lineToLinearHeading(
			Pose2d(38.0, -55.0, Math.toRadians(90.0)),
		).build();

		val path3 = drive.trajectorySequenceBuilder(path2.end()).lineToLinearHeading(
			Pose2d(path2.start().x, path2.start().y - 2, path2.start().heading)
		).build();

		drive.poseEstimate = path.start();

		waitForStart();

		specimenOuttake.collect();

		drive.followTrajectorySequence(path);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path2);

		claw.close();
		delay(1.0);

		specimenOuttake.collect();
		drive.followTrajectorySequence(path3);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();
	}

	/*private val drive = SampleMecanumDrive(hardwareMap);
	private val outtake = Outtake(hardwareMap);
	private val intake = Intake(hardwareMap.crservo.get("rotator0"), null, null);
	private val slide = Slide(hardwareMap);
	private val arm = Arm(hardwareMap.servo.get("arm"));
	private val hslide = HSlide(hardwareMap.servo.get("hslide"));
	private val claw = SpeciminClaw(hardwareMap);
	private val specimenOuttake = SpecimenOuttake(claw, slide);

	override fun runOpMode() {

		val preload = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(Vector2d(0.0, -28.0)).build();

		val samp1 = drive.trajectorySequenceBuilder(preload.end())
			.lineToLinearHeading(Pose2d(47.0, -45.0, Math.toRadians(90.0))).build();
		val samp2 = drive.trajectorySequenceBuilder(samp1.end())
			.lineToConstantHeading(Vector2d(59.0, -45.0)).build();
		val samp3 = drive.trajectorySequenceBuilder(samp2.end())
			.lineToConstantHeading(Vector2d(68.0, -45.0)).build();

		val specTake = drive.trajectorySequenceBuilder(samp3.end())
			.lineToConstantHeading(Vector2d(45.0, -60.0)).build();
		val score = drive.trajectorySequenceBuilder(Pose2d(45.0, -60.0, Math.toRadians(-90.0)))
			.lineToLinearHeading(Pose2d(0.0, -28.0, Math.toRadians(-90.0))).build();
		val specTake1 = drive.trajectorySequenceBuilder(score.end())
			.lineToLinearHeading(Pose2d(45.0, -60.0, Math.toRadians(90.0))).build();

		drive.poseEstimate = preload.start();

		outtake.armDown();
		outtake.bucketDown();
		claw.close();
		arm.down();

		waitForStart();
		specimenOuttake.collect();

		drive.followTrajectorySequence(preload);
		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		sampGrab(samp1);
		sampGrab(samp2);
		sampGrab(samp3);

		outtake.armDown();
		slide.runToPosition(0, 1.0);

		//spec1
		drive.followTrajectorySequence(specTake);
		specimenOuttake.collect();
		drive.followTrajectorySequence(score);
		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		//spec2
		drive.followTrajectorySequence(specTake1);
		specimenOuttake.collect();
		drive.followTrajectorySequence(score);
		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		//spec3
		drive.followTrajectorySequence(specTake1);
		specimenOuttake.collect();
		drive.followTrajectorySequence(score);
		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		//park
		drive.followTrajectorySequence(specTake1);
	}

	private fun sampGrab(samp: TrajectorySequence) {
		drive.followTrajectorySequence(samp);

		outtake.armDown();
		slide.runToPosition(0, 1.0);

		intake.forward();
		hslide.gotoPos(0.65);

		delay(0.5);

		hslide.score();
		arm.up();

		delay(0.375);
		intake.reverse();
		delay(0.375);

		intake.stop();
		arm.down();
		hslide.gotoPos(1.0);

		outtake.arm.position = 0.5;
		slide.runToPosition(-500, 1.0);
	}*/

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}