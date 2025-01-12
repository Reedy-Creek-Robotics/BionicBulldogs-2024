package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

@Autonomous
class AutoTEst : LinearOpMode()
{
	fun pos(x: Double, y: Double, h: Int): Pose2d
	{
		return Pose2d(x, y, Math.toRadians(-(h - 90).toDouble()));
	}

	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);

		val startPose = pos(7.5, -63.5, 180);
		val scorePos = pos(7.5, -32.0, 180);
		val wallPose = pos(42.5, -62.0, 0);

		val drive = MecanumDrive(hardwareMap, startPose);

		val builder = drive.actionBuilder(startPose);
		builder.lineToY(scorePos.position.y);
		builder.setTangent(0.0);
		builder.splineTo(wallPose.position, wallPose.heading);

		val action = builder.build();

		waitForStart();

		runBlocking(action);
	}
}