package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.XDrive
import org.firstinspires.ftc.teamcode.opmode.config.XDriveConfig

@TeleOp(group="test")
class XDriveTest : LinearOpMode()
{
	private lateinit var xDrive: XDrive;
	override fun runOpMode()
	{
		xDrive = XDrive(XDriveConfig(hardwareMap));
		
		waitForStart();
		while(opModeIsActive())
		{
			val forward = -gamepad1.left_stick_y;
			val right = gamepad1.left_stick_x;
			val rotate = -gamepad1.right_stick_x;
			xDrive.drive(forward, right, rotate);
		}
	}
}