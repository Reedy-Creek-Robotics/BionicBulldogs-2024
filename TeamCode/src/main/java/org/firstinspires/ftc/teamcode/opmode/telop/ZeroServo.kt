package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx

@TeleOp
class ZeroServo: LinearOpMode()
{
	override fun runOpMode()
	{
		val s = hardwareMap.servo.get("servo");
		s.position = 0.0;
		var posA = 0.0;
		var posB = 1.0;

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		while(opModeIsActive())
		{
			if(gamepad.dpadUp())
			{
				posB += 0.05;
			}
			if(gamepad.dpadUp())
			{
				posB -= 0.05;
			}
			if(gamepad.cross())
			{
				s.position = posA;
			}
			if(gamepad.circle())
			{
				s.position = posB;
			}
			gamepad.copy();
			telemetry.addData("position", posB);
			telemetry.update();
		}
	}
}