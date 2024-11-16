package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.drive.TwoWheelTrackingLocalizer

@TeleOp
class LocalizationTestNoDrive: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);

		drive.poseEstimate = Pose2d(0.0, 0.0, 0.0);

		waitForStart();
		while(opModeIsActive())
		{
			drive.update();

			telemetry.addData("x", drive.poseEstimate.x);
			telemetry.addData("y", drive.poseEstimate.y);
			telemetry.addData("heading raw", Math.toDegrees(drive.externalHeading));
			telemetry.addData("heading", Math.toDegrees(drive.poseEstimate.heading));
			telemetry.update();
		}
	}
}