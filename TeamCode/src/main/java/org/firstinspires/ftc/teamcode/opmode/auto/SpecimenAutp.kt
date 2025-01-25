package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.Logging
import org.firstinspires.ftc.teamcode.modules.actions.HSlideAction_GotoPos
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Grab
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Score
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.PI

@Autonomous
class SpecimenAutp: LinearOpMode()
{
	override fun runOpMode()
	{
		val logger = Logging();

		MecanumDrive.PARAMS.maxWheelVel = 80.0;
		MecanumDrive.PARAMS.maxProfileAccel = 60.0;
		MecanumDrive.PARAMS.maxAngVel = PI * 4.0;
		MecanumDrive.PARAMS.maxAngAccel = PI * 4.0;

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
				SleepAction(0.5),
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
					.splineToLinearHeading(Pose2d(32.0, -5.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(36.0)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.2),
				drive.actionBuilder(
					Pose2d(36.0, -6.5, Math.toRadians(180.0)),
					velOverrideRaw(100.0),
					accelOverrideRaw(minAccel = -75.0, maxAccel = 75.0)
				)
					.setTangent(Math.toRadians(-180.0))
					.splineToConstantHeading(Vector2d(32.0, -36.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(Vector2d(56.0, -46.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(180.0))
					.lineToX(17.0)
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(Vector2d(56.0, -54.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(180.0))
					.lineToX(17.0)
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
					.splineToLinearHeading(Pose2d(32.0, -1.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(35.5)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.2),
				drive.actionBuilder(Pose2d(35.0, -1.5, Math.toRadians(180.0)))
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
					.splineToLinearHeading(Pose2d(32.0, 1.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(35.5)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.2),
				HSlideAction_GotoPos(HSlide.min),
				drive.actionBuilder(Pose2d(36.0, -2.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-135.0))
					.splineToLinearHeading(
						Pose2d(22.0, -30.0, Math.toRadians(-135.0)),
						Math.toRadians(-135.0)
					)
					.build()
			)
		);

		waitForStart();

		val dash = FtcDashboard.getInstance();
		val c = Canvas();
		action.preview(c);

		var b = true;
		while(b && !Thread.currentThread().isInterrupted)
		{
			val p = TelemetryPacket();
			p.fieldOverlay().operations.addAll(c.operations);

			b = action.run(p);

			dash.sendTelemetryPacket(p);

			logger.posX.set(drive.localizer.pose.position.x);
			logger.posY.set(drive.localizer.pose.position.y);
			logger.posH.set(drive.localizer.pose.heading.toDouble());
			logger.state.set(action::class.java.simpleName);
			logger.update();
		}

		rotPos = drive.localizer.pose.heading.toDouble();
	}
}