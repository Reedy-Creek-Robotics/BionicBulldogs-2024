package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.IMULocalizer
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.ImuEx
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp(group = "test")
class FRHDriveTest: LinearOpMode()
{

	//Drivetrain
	private lateinit var hDrive: HDrive;

	override fun runOpMode()
	{
		hDrive = HDrive(HDriveConfig(hardwareMap));

		//Swapped internal localizer to external
		//val imu = hardwareMap.get(SparkFunOTOS::class.java, "imu2");
		//hDrive.setLocalizer(SparkfunImuLocalizer(imu));
		hDrive.setLocalizer(IMULocalizer(ImuEx(hardwareMap.get(IMU::class.java, "imu"))));

		waitForStart();

		while(opModeIsActive())
		{
			val forward = gamepad1.left_stick_y;
			val right = gamepad1.left_stick_x;
			val rotate = gamepad1.right_stick_x;

			hDrive.driveFR(forward, right, rotate);
			hDrive.telem(telemetry);

			telemetry.addData("forward power:", forward);
			telemetry.addData("strafe power:", right);
			telemetry.addData("rotate power:", rotate);

			telemetry.update()

		}
	}
}