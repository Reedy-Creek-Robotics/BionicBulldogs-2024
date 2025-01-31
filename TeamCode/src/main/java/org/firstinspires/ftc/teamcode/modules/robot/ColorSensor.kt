package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

class ColorSensor(hardwareMap: HardwareMap, private val gamepad: Gamepad, private val badColor: Int)
{
	companion object
	{
		const val RED = 1;
		const val BLUE = 2;
		const val YELLOW = 3;
		const val NONE = 4;
	}

	private val sensor = hardwareMap.get(RevColorSensorV3::class.java, "colorSensor");
	var col = RED;

	init
	{
		sensor.enableLed(true);
	}

	fun update()
	{
		val r = sensor.red();
		val g = sensor.green();
		val b = sensor.blue();

		if(r > 800 && g < 800)
		{
			if(col == NONE && badColor != RED)
				gamepad.rumble(0.5, 0.0, 500);
			col = RED;
		}
		else if(b > 800)
		{
			if(col == NONE && badColor != BLUE)
				gamepad.rumble(0.5, 0.0, 500);
			col = BLUE;
		}
		else if(r > 800 && g > 800)
		{
			if(col == NONE && badColor != YELLOW)
				gamepad.rumble(0.0, 0.5, 500);
			col = YELLOW;
		}
		else
		{
			if(col == badColor)
				gamepad.stopRumble();
			col = NONE;
		}
		if(col == badColor)
		{
			gamepad.rumble(1.0, 1.0, 50);
		}
	}

	fun telem(telemetry: Telemetry)
	{
		telemetry.addData("r", sensor.red());
		telemetry.addData("g", sensor.green());
		telemetry.addData("b", sensor.blue());
		telemetry.addData("color", col);
	}
}