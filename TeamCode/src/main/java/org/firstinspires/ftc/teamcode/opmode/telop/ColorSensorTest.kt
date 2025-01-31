package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.robot.ColorSensor

@TeleOp
class ColorSensorTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val colorSensor = ColorSensor(hardwareMap, gamepad1, ColorSensor.RED);
		waitForStart();
		while(opModeIsActive())
		{
			colorSensor.update();
			colorSensor.telem(telemetry);
			telemetry.update();
		}
	}
}