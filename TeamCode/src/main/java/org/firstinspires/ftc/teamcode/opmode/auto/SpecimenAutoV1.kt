package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

//Working 1+1 Auto
//w/ parking
@Autonomous
@Config
class SpecimenAutoV1: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);
		val claw = SpeciminClaw(hardwareMap);
		val slides = Slide(hardwareMap);
		val outtake = Outtake(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"));
    val specimenOuttake = SpecimenOuttake(claw, slides);

		val path = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(Vector2d(0.0, -28.0)).build();
		val path2 = drive.trajectorySequenceBuilder(path.end())
			.lineToLinearHeading(
        Pose2d(38.0, -54.0, Math.toRadians(90.0)),
        velOverride(),
        accelOverride(maxAccel = 30.0)
      )
			.build();
		val path3 = drive.trajectorySequenceBuilder(path2.end())
			.lineToLinearHeading(Pose2d(path.end().x + 2, path.end().y - 5, path.end().heading))
			.lineToLinearHeading(Pose2d(path.end().x + 2, path.end().y, path.end().heading))
			.build();
		val path4 = drive.trajectorySequenceBuilder(path3.end())
			.lineToLinearHeading(Pose2d(38.0, -56.0, 0.0))
			.build();

		drive.poseEstimate = path.start();

    val initPos = drive.poseEstimate.heading;

		hslide.zero();
    claw.close();
		outtake.bucketDown();
		outtake.armDown();

		waitForStart();

		slides.raise();

		drive.followTrajectorySequence(path);

    specimenOuttake.score();
    specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path2);

    specimenOuttake.collect();
    specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path3);

    specimenOuttake.score();
    specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path4);

		rotPos = drive.poseEstimate.heading - initPos;
	}

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}
