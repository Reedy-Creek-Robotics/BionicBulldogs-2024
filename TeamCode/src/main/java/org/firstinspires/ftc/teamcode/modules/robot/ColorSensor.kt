package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap

class ColorSensor(hardwareMap: HardwareMap, private val gamepad: Gamepad, private val badColor: Int)
{
	companion object
	{
		val Red = 1;
		val Blue = 2;
		val Yellow = 3;
		val None = 4;
	}

	private val sensor = hardwareMap.get(RevColorSensorV3::class.java, "colorSensor");
	private var col = Red;

	init
	{
		sensor.enableLed(true);
	}

	fun update()
	{
		val r = sensor.red();
		val g = sensor.green();
		val b = sensor.blue();

		if(r > 500 && g < 500 && b < 500)
		{
			if(col == None && badColor != Red)
				gamepad.rumble(0.5, 0.0, 500);
			col = Red;
		}
		else if(r < 500 && g < 500 && b > 500)
		{
			if(col == None && badColor != Blue)
				gamepad.rumble(0.5, 0.0, 500);
			col = Blue;
		}
		else if(r > 500 && g > 500 && b < 500)
		{
			if(col == None && badColor != Yellow)
				gamepad.rumble(0.0, 0.5, 500);
			col = Yellow;
		}
		else
		{
			if(col == badColor)
				gamepad.stopRumble();
			col = None;
		}
		if(col == badColor)
		{
			gamepad.rumble(1.0, 1.0, 50);
		}
	}
}