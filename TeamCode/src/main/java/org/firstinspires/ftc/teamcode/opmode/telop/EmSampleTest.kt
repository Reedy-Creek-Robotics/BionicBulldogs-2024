package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Gripper
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.Spin
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class EmSampleTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val gripper = Gripper(hardwareMap.servo.get("servo"));

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