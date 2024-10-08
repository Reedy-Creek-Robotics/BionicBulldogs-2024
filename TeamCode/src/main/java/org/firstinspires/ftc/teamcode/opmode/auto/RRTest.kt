package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive


@Autonomous
class RRTest : LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);
		val path = drive.trajectorySequenceBuilder(Pose2d(12.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(Vector2d(0.0, -32.0)).waitSeconds(1.0)
			.lineToConstantHeading(Vector2d(12.0, -60.0)).build();
		drive.poseEstimate = path.start();

		waitForStart();

		drive.followTrajectorySequence(path);
	}
}