package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class ColorSensor(hardwareMap: HardwareMap, private val gamepad: Gamepad, private val badColor: Int)
{
	companion object
	{
		const val RED = 1;
		const val BLUE = 2;
		const val YELLOW = 3;
		const val NONE = 4;
		const val UNKNOWN = 5;
	}

	private val sensor = hardwareMap.get(RevColorSensorV3::class.java, "colorSensor");
	var col = NONE;
	private var prevCol = NONE;

	init
	{
		sensor.enableLed(true);
	}

	fun update()
	{
		prevCol = col;
		if(sensor.getDistance(DistanceUnit.MM) < 20)
		{
			val r = sensor.red();
			val g = sensor.green();
			val b = sensor.blue();

			if(r > 800 && g < 800)
			{
				col = RED;
			}
			else if(b > 800)
			{
				col = BLUE;
			}
			else if(r > 800 && g > 800)
			{
				col = YELLOW;
			}
			else
			{
				if(col == badColor)
					gamepad.stopRumble();
				col = UNKNOWN;
			}
		}
		else
			col = NONE;

		if(isNone(prevCol) && badColor != BLUE && col == BLUE)
			gamepad.rumble(0.5, 0.0, 500);
		if(isNone(prevCol) && badColor != RED && col == RED)
			gamepad.rumble(0.5, 0.0, 500);
		if(isNone(prevCol) && badColor != YELLOW && col == YELLOW)
			gamepad.rumble(0.0, 0.5, 500);
		if(col == badColor)
			gamepad.rumble(1.0, 1.0, 50);
	}

	fun telem(telemetry: Telemetry)
	{
		telemetry.addData("(color sensor) r", sensor.red());
		telemetry.addData("(color sensor) g", sensor.green());
		telemetry.addData("(color sensor) b", sensor.blue());
		telemetry.addData("(color sensor) dist", sensor.getDistance(DistanceUnit.MM));
		telemetry.addData("(color sensor) color", col);
	}

	private fun isNone(c: Int): Boolean
	{
		return c == NONE || c == UNKNOWN;
	}
}