package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.*
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

@Autonomous
class SampleAuto: LinearOpMode()
{
	override fun runOpMode()
	{
		initComponents(hardwareMap);
		specimenClaw.open();
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);

		val beginPose = Pose2d(7.5, 30.5, Math.toRadians(-90.0))
		val drive = MecanumDrive(hardwareMap, beginPose)

		val action = SequentialAction(
			listOf(
				ParallelAction(
					SampleOuttakeAction_Grab(),
					drive.actionBuilder(beginPose)
						.setTangent(0.0)
						.splineToLinearHeading(Pose2d(15.5, 56.0, Math.toRadians(-45.0)), Math.toRadians(135.0))
						.build()
				),
				SampleOuttakeAction_Score(),
				ParallelAction(
					SequentialAction(
						IntakeAction_Intake(),
						HSlideAction_GotoPos(HSlide.min),
						ArmAction_Down(),
						SleepAction(0.25),
						IntakeAction_SetRotation(Intake.rotatorCenter + Intake.autoRotatorIncrement),
					),
					drive.actionBuilder(Pose2d(15.5, 56.0, Math.toRadians(-45.0)))
						.setTangent(Math.toRadians(-45.0))
						.splineToLinearHeading(Pose2d(25.0, 43.5, Math.toRadians(0.0)), Math.toRadians(0.0))
						.setTangent(Math.toRadians(90.0))
						.lineToY(45.0)
						.build(),
				),
				IntakeAction_ZeroRotator(),
				SleepAction(0.25),
				ArmAction_Up(),
				HSlideAction_Score(),
				SleepAction(0.5),
				IntakeAction_Outtake(),
				SleepAction(0.6),
				IntakeAction_Stop(),
				ParallelAction(
					SampleOuttakeAction_Grab(),
					SequentialAction(
						SleepAction(0.2),
						drive.actionBuilder(Pose2d(25.0, 45.0, Math.toRadians(0.0)))
							.setTangent(Math.toRadians(135.0))
							.splineToLinearHeading(
								Pose2d(16.5, 55.0, Math.toRadians(-45.0)),
								Math.toRadians(135.0)
							)
							.build()
					)
				),
				SampleOuttakeAction_Score(),
				ParallelAction(
					SequentialAction(
						IntakeAction_Intake(),
						HSlideAction_GotoPos(HSlide.min),
						ArmAction_Down(),
						SleepAction(0.25),
						IntakeAction_SetRotation(Intake.rotatorCenter + Intake.autoRotatorIncrement),
					),
					drive.actionBuilder(Pose2d(16.5, 55.0, Math.toRadians(-45.0)))
						.setTangent(Math.toRadians(-45.0))
						.splineToLinearHeading(Pose2d(25.0, 52.0, Math.toRadians(0.0)), Math.toRadians(0.0))
						.setTangent(Math.toRadians(90.0))
						.lineToY(53.0)
						.build(),
				),
				IntakeAction_ZeroRotator(),
				SleepAction(0.25),
				ArmAction_Up(),
				HSlideAction_Score(),
				SleepAction(0.5),
				IntakeAction_Outtake(),
				SleepAction(0.6),
				IntakeAction_Stop(),
				ParallelAction(
					SampleOuttakeAction_Grab(),
					SequentialAction(
						SleepAction(0.6),
						drive.actionBuilder(Pose2d(25.0, 53.0, Math.toRadians(0.0)))
							.setTangent(Math.toRadians(135.0))
							.splineToLinearHeading(
								Pose2d(16.5, 55.5, Math.toRadians(-45.0)),
								Math.toRadians(135.0)
							)
							.build()
					)
				),
				SampleOuttakeAction_Score(),
				ParallelAction(
					SequentialAction(
						IntakeAction_Intake(),
						HSlideAction_GotoPos(HSlide.max / 2),
						ArmAction_Down(),
					),
					drive.actionBuilder(Pose2d(16.5, 55.5, Math.toRadians(-45.0)))
						.setTangent(Math.toRadians(-45.0))
						.splineToLinearHeading(Pose2d(43.75, 49.0, Math.toRadians(90.0)), Math.toRadians(0.0))
						.build(),
				),
				HSlideAction_GotoPos(HSlide.min),
				SleepAction(1.0)
			)
		);

		waitForStart()

		runBlocking(action);
	}
}