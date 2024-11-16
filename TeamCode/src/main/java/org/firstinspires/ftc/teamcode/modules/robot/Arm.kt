package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class Arm(private val arm: Servo)
{

	enum class State
	{
		Down, Up
	}

	var state: State = State.Up

	companion object
	{
		@JvmField
//		var armDown: Double = 0.92;
		var armDown: Double = 0.93;

		@JvmField
		var armUp: Double = 0.23;
	}

	fun down()
	{
		arm.position = armDown;
		state = State.Down;
	}

	fun up()
	{
		arm.position = armUp;
		state = State.Up;
	}
}