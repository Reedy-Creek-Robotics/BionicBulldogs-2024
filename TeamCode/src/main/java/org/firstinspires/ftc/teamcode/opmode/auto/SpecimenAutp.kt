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
import org.firstinspires.ftc.teamcode.modules.actions.HSlideAction_GotoPos
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Grab
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Score
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

@Autonomous
class SpecimenAutp: LinearOpMode()
{
	override fun runOpMode()
	{
		//MecanumDrive.PARAMS.maxWheelVel = 75.0;
		//MecanumDrive.PARAMS.maxProfileAccel = 85.0;
		initComponents(hardwareMap);
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);

		val beginPose = Pose2d(7.0, -7.5, Math.toRadians(180.0))
		val drive = MecanumDrive(hardwareMap, beginPose)

		val action = SequentialAction(
			listOf(
				SpecimenOuttakeAction_Grab(),
				drive.actionBuilder(beginPose)
					.lineToX(36.0)
					.build(),
				SpecimenOuttakeAction_Score(),
				drive.actionBuilder(Pose2d(36.0, -7.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-180.0))
					.lineToX(32.0)
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
					.lineToX(36.0)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.2),
				drive.actionBuilder(Pose2d(36.0, -6.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-180.0))
					.splineToConstantHeading(Vector2d(32.0, -36.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(
						Vector2d(56.0, -46.0), Math.toRadians(-90.0)
					)
					.setTangent(
						Math.toRadians(180.0),
					)
					.lineToX(
						17.0
					)
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(
						Vector2d(56.0, -56.0), Math.toRadians(-90.0)
					)
					.setTangent(
						Math.toRadians(180.0)
					)
					.lineToX(
						17.0
					)
					.build(),
				drive.actionBuilder(Pose2d(17.0, -56.0, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(90.0))
					.splineToLinearHeading(Pose2d(14.5, -40.5, 0.0), Math.toRadians(-135.0))
					.setTangent(0.0)
					.lineToX(8.0)
					.build(),
				SpecimenOuttakeAction_Grab(),
				drive.actionBuilder(Pose2d(8.5, -40.5, 0.0))
					.setTangent(Math.toRadians(45.0))
					.splineToLinearHeading(Pose2d(32.0, -2.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(36.0)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.2),
				HSlideAction_GotoPos(HSlide.min),
				drive.actionBuilder(Pose2d(36.0, -2.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-135.0))
					.splineToLinearHeading(Pose2d(22.0, -30.0, Math.toRadians(-135.0)), Math.toRadians(-135.0))
					.build()
			)
		);

		waitForStart()
		runBlocking(action);
		rotPos = drive.localizer.pose.heading.toDouble();
	}
}