package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class SpeciminClaw(private val claw: Servo)
{

	enum class State {
		Closed,
		Open
	}

	var state: State = State.Closed;

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
		state = State.Open;
	}

	fun close()
	{
		claw.position = closePos;
		state = State.Closed;
	}
}