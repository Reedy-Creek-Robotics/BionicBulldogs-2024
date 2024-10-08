package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Claw
import org.firstinspires.ftc.teamcode.modules.robot.Slides
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

class ClawTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val claw = Claw(hardwareMap.servo.get("claw"));
		val slides = Slides(hardwareMap.dcMotor.get("slide"));
		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		slides.reverse();

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		claw.close();

		while(opModeIsActive())
		{
			gamepad.copy();

			// drive
			drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

			// claw
			if(gamepad.dpadLeft())
			{
				claw.close()
			};

			if(gamepad.dpadRight())
			{
				claw.open()
			};

			// slides
			if(gamepad1.dpad_up)
			{
				slides.up()
			};

			if(gamepad1.dpad_down)
			{
				slides.up()
			};

			if(!gamepad1.dpad_up && !gamepad1.dpad_down)
			{
				slides.stop()
			};
		}
	}
}