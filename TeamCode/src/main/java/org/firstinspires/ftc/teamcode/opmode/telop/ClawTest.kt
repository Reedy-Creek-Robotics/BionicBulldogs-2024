package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig
import org.firstinspires.ftc.teamcode.modules.robot.Slide

@TeleOp
class ClawTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val speciminClaw = SpeciminClaw(hardwareMap.servo.get("claw"));
		val slide = Slide(hardwareMap.dcMotor.get("slide") as DcMotorEx);
		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		slide.reverse();

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		speciminClaw.close();

		while(opModeIsActive())
		{
			gamepad.copy();

			//drive
			drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

			//claw
			if(gamepad.dpadRight())
			{
				if(speciminClaw.state == SpeciminClaw.State.Closed)
				{
					speciminClaw.open();
				}
				else if(speciminClaw.state == SpeciminClaw.State.Open)
				{
					speciminClaw.close();
				}
			}

			//slides
			if(gamepad1.dpad_up)
			{
				slide.up();
			}

			if(gamepad1.dpad_down)
			{
				slide.down();
			}

			if(!gamepad1.dpad_up && !gamepad1.dpad_down)
			{
				slide.stop()
			}
		}
	}
}