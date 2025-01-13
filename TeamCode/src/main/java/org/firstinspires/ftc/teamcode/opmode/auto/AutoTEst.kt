package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Grab
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Score
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

@Autonomous
class AutoTEst: LinearOpMode()
{
	fun pos(x: Double, y: Double, h: Int): Pose2d
	{
		return Pose2d(x, y, Math.toRadians(-(h - 90).toDouble()));
	}

	override fun runOpMode()
	{
		initComponents(hardwareMap);
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);

		val beginPose = Pose2d(7.0, -7.5, Math.toRadians(180.0))
		val drive = MecanumDrive(hardwareMap, beginPose)

		val action = SequentialAction(
			listOf(
				SpecimenOuttakeAction_Grab(),
				drive.actionBuilder(beginPose)
					.lineToX(34.5)
					.build(),
				SpecimenOuttakeAction_Score(),
				drive.actionBuilder(Pose2d(34.5, -7.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-135.0))
					.splineToLinearHeading(Pose2d(14.5, -40.5, 0.0), Math.toRadians(-135.0))
					.setTangent(0.0)
					.lineToX(8.0)
					.build(),
				SpecimenOuttakeAction_Grab(),
				drive.actionBuilder(Pose2d(8.5, -40.5, 0.0))
					.setTangent(Math.toRadians(45.0))
					.splineToLinearHeading(Pose2d(32.0, -6.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(35.5)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.2),
				drive.actionBuilder(Pose2d(35.5, -6.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-90.0))
					.lineToY(-36.0)
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(Vector2d(56.0, -46.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(180.0))
					.lineToX(20.0)
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(Vector2d(56.0, -56.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(180.0))
					.lineToX(20.0)
					.build()
			)
		);

		waitForStart()

		runBlocking(action);
	}
}