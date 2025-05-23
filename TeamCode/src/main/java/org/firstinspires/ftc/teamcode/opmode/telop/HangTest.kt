package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Slide

@TeleOp
class HangTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val slide = Slide(hardwareMap);
		//val drive = HDrive(HDriveConfig(hardwareMap));
		//drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		//slide.reverse();

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		while(opModeIsActive())
		{
			gamepad.copy();

			//drive
			//drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

			if(gamepad.cross()) {
				if (slide.getPos() >= -100) {
					slide.gotoPos(-2450)
				} else if (slide.getPos() <= -100) {
					slide.gotoPos(0)
				}
			}

			while (gamepad.leftTriggerb()) {
				slide.up()
				//make slide go down
			}

			telemetry.addData("pos", slide.getPos())
			telemetry.update()
		}
	}
}