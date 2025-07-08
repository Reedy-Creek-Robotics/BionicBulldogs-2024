package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class ColorSensor(hardwareMap: HardwareMap, private val gamepad: Gamepad? = null, private val badColor: Int = 0)
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
		//for some reason, the sensor thinks that blue samples are farther than red/yellow
		if(sensor.getDistance(DistanceUnit.MM) < 25)
		{
			val r = sensor.red();
			val g = sensor.green();
			val b = sensor.blue();
//225, 430, 576
//217, 413, 520
//B: 750, 670, 700, 400, 690, 730

			//Blue
			//min (225, 500, 700)
//260, 550, 750
//270, 520, 750
//265, 515, 740
//250, 495, 745
//250, 489, 730

			//Red
			//min (500, 525, 450)
//540, 520, 480
//545, 530, 450
//540, 530, 460
//300, 385, 375 (x)
//188, 345, 359 (?)
//570, 545, 445
//590, 550, 450

			//Yellow
			//min (750, 1000, 500)
//778, 1128, 534
//785, 1130, 545
//790, 1126, 550
//803, 1143, 548
//806, 1140, 545



			//red
			// 1150 640 350
			// 5400 3700 2400

			//blue
			// 860 1700 3450

			//yellow
			// 3100 4280 1170
			// 5200 7100 1750

			if(r > 4000 && g < 4000 && b < 4000)
			{
				col = RED;
			}
			else if(b > 3000 && r < 2000)
			{
				col = BLUE;
			}
			else if(g > 4000 && r > 4000)
			{
				col = YELLOW;
			}
			else
			{
				if(col == badColor)
					gamepad?.stopRumble();
				col = UNKNOWN;
			}
		}
		else
			col = NONE;

		if(gamepad != null)
		{
			if(isNone(prevCol) && badColor != BLUE && col == BLUE)
				gamepad.rumble(0.5, 0.0, 500);
			if(isNone(prevCol) && badColor != RED && col == RED)
				gamepad.rumble(0.5, 0.0, 500);
			if(isNone(prevCol) && badColor != YELLOW && col == YELLOW)
				gamepad.rumble(0.0, 0.5, 500);
			if(col == badColor)
				gamepad.rumble(1.0, 1.0, 50);
		}
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