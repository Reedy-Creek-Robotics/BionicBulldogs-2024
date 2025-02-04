package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class SpecimenRotator(hardwareMap: HardwareMap)
{
	companion object
	{
		@JvmField
		var down = 0.0;

		@JvmField
		var up = 0.2;
	}

	var state = 1;

	private val servo = hardwareMap.servo["specimenRotator"];

	fun init()
	{
		servo.position = down;
	}

	fun toggle()
	{
		if(state == 0)
		{
			state = 1;
			servo.position = down;
		}
		else
		{
			state = 0;
			servo.position = up;
		}
	}
}