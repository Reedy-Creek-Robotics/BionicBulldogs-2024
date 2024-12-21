package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class SpeciminClaw(hardwareMap: HardwareMap)
{
	private val claw = hardwareMap.servo.get("claw");

	enum class State {
		Closed,
		Open
	}

	var state: State = State.Closed;

	companion object
	{
		@JvmField
		var openPos: Double = 0.375;
		@JvmField
		var closePos: Double = 0.0;
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
