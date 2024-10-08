package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.Servo

class Claw(private val claw: Servo)
{
	companion object
	{
		@JvmField
		var openPos: Double = 0.75;
		@JvmField
		var closePos: Double = 0.4;
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