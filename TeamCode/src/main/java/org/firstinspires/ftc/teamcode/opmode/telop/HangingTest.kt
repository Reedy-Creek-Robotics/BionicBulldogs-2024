package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class HangingTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = HDrive(HDriveConfig(hardwareMap));

		val hangingMotor = hardwareMap.dcMotor.get("slide");
		val hangingMotor2 = hardwareMap.dcMotor.get("slide2");

		hangingMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
		hangingMotor2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
		hangingMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		hangingMotor2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;

		waitForStart();

		while(opModeIsActive())
		{
			val forward = gamepad1.left_stick_y;
			val right = gamepad1.left_stick_x;
			val rotate = gamepad1.right_stick_x;

			drive.drive(forward, right, rotate);

			hangingMotor.power = (gamepad1.left_trigger - gamepad1.right_trigger).toDouble();
			hangingMotor2.power = (gamepad1.left_trigger - gamepad1.right_trigger).toDouble();
		}
	}
}