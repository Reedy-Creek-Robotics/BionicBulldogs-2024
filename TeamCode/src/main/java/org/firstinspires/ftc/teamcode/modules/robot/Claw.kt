package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class Claw(private val claw: Servo)
{
	companion object
	{
		@JvmField
		var openPos: Double = 0.75;
		@JvmField
		var closePos: Double = 0.3;
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