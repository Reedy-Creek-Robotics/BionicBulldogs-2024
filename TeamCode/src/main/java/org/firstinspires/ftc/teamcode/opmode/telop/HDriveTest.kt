package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp(group = "test")
class HDriveTest: LinearOpMode()
{
		private lateinit var hDrive: HDrive;
		override fun runOpMode()
		{
				hDrive = HDrive(HDriveConfig(hardwareMap));

				waitForStart();
				while(opModeIsActive())
				{
						val forward = gamepad1.left_stick_y;
						val right = gamepad1.left_stick_x;
						val rotate = gamepad1.right_stick_x;
						hDrive.drive(forward, right, rotate);
						hDrive.telem(telemetry);
				}
		}
}