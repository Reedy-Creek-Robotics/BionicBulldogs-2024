package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.Servo

class Claw(private val claw: Servo)
{
	companion object
	{
		@JvmField
		var openPos: Double = 1.0;
		@JvmField
		var closePos: Double = 0.0;
	}

	fun open()
	{
		claw.position = openPos;
	}

	fun close()
	{
		claw.position = closePos;
	}
}