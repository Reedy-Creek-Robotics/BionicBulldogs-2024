package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.modules.drive.IMULocalizer
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.hardware.ImuEx
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp(group = "test")
class FRHDriveTest: LinearOpMode()
{
		private lateinit var arm: Servo
		private lateinit var _rotate: Servo
		private lateinit var hDrive: HDrive


		override fun runOpMode()
		{
				hDrive = HDrive(HDriveConfig(hardwareMap))

				arm = hardwareMap.servo.get("arm")
				_rotate = hardwareMap.servo.get("rotate")
				val imu = ImuEx(hardwareMap.get(IMU::class.java, "imu"))

				hDrive.setLocalizer(IMULocalizer(imu))

				waitForStart()
				while(opModeIsActive())
				{
						val forward = gamepad1.left_stick_y
						val right = gamepad1.left_stick_x
						val rotate = gamepad1.right_stick_x
						hDrive.driveFR(forward, right, rotate)
						hDrive.telem(telemetry)

						if (gamepad1.left_bumper) {
							arm.position = 0.0
						}
						if (gamepad1.right_bumper) {
							arm.position = 0.5
						}
						if (gamepad1.x) {
							_rotate.position = 0.0
						}
						if (gamepad1.b) {
							_rotate.position = 0.25
						}

				}
		}
}