package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class HangTest3 : LinearOpMode()
{
	override fun runOpMode()
	{
		val slide = Slide(hardwareMap);
		val gamepad = GamepadEx(gamepad1);
		var state = 0;

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));
		drive.setPosEstimate(Pose2d(0.0, 0.0, rotPos));

		waitForStart();
		while(opModeIsActive())
		{
			gamepad.copy();
			drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
			if(gamepad.cross())
			{
				when(state)
				{
					0 ->
					{
						slide.gotoPos(-2450);
						state = 1;
					}
					1 -> {
						slide.gotoPos(0);
						state = 0;
					}
				}
			}
			if(gamepad1.right_trigger > 0.5)
			{
				slide.runWithoutEncoder();
				slide.down();
				state = 3;
			}
			else if(state == 3)
			{
				slide.runWithoutEncoder();
				slide.stop();
				state = 0;
			}
		}
	}
}