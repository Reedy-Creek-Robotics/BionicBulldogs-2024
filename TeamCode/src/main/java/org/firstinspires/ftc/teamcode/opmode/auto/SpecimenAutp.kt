
package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.*
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.*
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import java.io.File
import java.io.FileWriter
import kotlin.math.PI

@Autonomous
class SpecimenAutp: LinearOpMode()
{
	override fun runOpMode()
	{
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
				drive.actionBuilder(beginPose, drive.defaultVelConstraint, accelOverrideRaw(minAccel = -20.0))
					.lineToX(36.125)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.1),
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
					.lineToX(36.25)
					.build(),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.1),
				drive.actionBuilder(
					Pose2d(36.0, -6.5, Math.toRadians(180.0)),
					velOverrideRaw(100.0),
					accelOverrideRaw(minAccel = -75.0, maxAccel = 75.0)
				)
					.setTangent(Math.toRadians(-180.0))
					.splineToConstantHeading(Vector2d(32.0, -36.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(Vector2d(55.0, -47.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(180.0))
					.lineToX(17.0)
					.setTangent(Math.toRadians(0.0))
					.splineToConstantHeading(Vector2d(56.0, -55.0), Math.toRadians(-90.0))
					.setTangent(Math.toRadians(180.0))
					.lineToX(17.0)
					.build(),
				drive.actionBuilder(Pose2d(17.0, -56.0, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(90.0))
					.splineToLinearHeading(Pose2d(14.5, -40.5, 0.0), Math.toRadians(-135.0))
					.setTangent(0.0)
					.lineToX(7.75)
					.build(),
				SpecimenOuttakeAction_Grab(),
				drive.actionBuilder(Pose2d(8.5, -40.5, 0.0))
					.setTangent(Math.toRadians(45.0))
					.splineToLinearHeading(Pose2d(32.0, -1.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(36.25)
					.build(),
				SleepAction(0.05),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.1),
				drive.actionBuilder(Pose2d(35.0, -1.5, Math.toRadians(180.0)))
					.setTangent(Math.toRadians(-180.0))
					.lineToX(32.0)
					.setTangent(Math.toRadians(-135.0))
					.splineToLinearHeading(Pose2d(14.5, -40.5, 0.0), Math.toRadians(-135.0))
					.setTangent(0.0)
					.lineToX(7.75)
					.build(),
				SpecimenOuttakeAction_Grab(),
				drive.actionBuilder(Pose2d(8.5, -40.5, 0.0))
					.setTangent(Math.toRadians(45.0))
					.splineToLinearHeading(Pose2d(32.0, 1.5, Math.toRadians(180.0)), Math.toRadians(45.0))
					.setTangent(0.0)
					.lineToX(36.00)
					.build(),
				SleepAction(0.1),
				SpecimenOuttakeAction_Score(),
				SleepAction(0.6),
				//HSlideAction_GotoPos(HSlide.min),
				/*drive.actionBuilder(Pose2d(36.0, -2.5, Math.toRadians(180.0)), velOverrideRaw(100.0), accelOverrideRaw(-75.0, 75.0))
					.setTangent(Math.toRadians(-135.0))
					.splineToLinearHeading(
						Pose2d(22.0, -30.0, Math.toRadians(-135.0)),
						Math.toRadians(-135.0)
					)
					.build()*/
			)
		);

		waitForStart();

		val timerAction = toTimerAction(action);
		runBlocking(timerAction);
		rotPos = drive.localizer.pose.heading.toDouble();

		val file = File("/sdcard/opmodeTimerSpecimen.txt");
		if(!file.exists())
			file.createNewFile();
		val writer = FileWriter(file);
		writer.write(timerAction.timerString());
		writer.close();


	}
}
