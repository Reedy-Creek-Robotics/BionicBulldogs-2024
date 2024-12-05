package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Gripper
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw

@TeleOp
class SpecimenTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val gripper = SpeciminClaw(hardwareMap);

		waitForStart();

		val gamepad = GamepadEx(gamepad1);

		while(opModeIsActive())
		{
			gamepad.copy();

			if(gamepad1.dpad_up)
			{
				gripper.open();
			}

			if(gamepad1.dpad_down)
			{
				gripper.close();
			}
		}
	}
}