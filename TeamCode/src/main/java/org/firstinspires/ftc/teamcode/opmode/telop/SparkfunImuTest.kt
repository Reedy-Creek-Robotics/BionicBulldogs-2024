package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class SparkfunImuTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val imu = hardwareMap.get(SparkFunOTOS::class.java, "externalImu");

		val drive = HDrive(HDriveConfig(hardwareMap));

		drive.setLocalizer(SparkfunImuLocalizer(imu));

		waitForStart()

		while(opModeIsActive())
		{
			val forward = gamepad1.left_stick_y;
			val right = gamepad1.left_stick_x;
			val rotate = gamepad1.right_stick_x;
			drive.driveFR(forward, right, rotate);
			telemetry.addData("angle", imu.position.h);
			telemetry.update();
		}
	}
}