package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class Arm(hardwareMap: HardwareMap)
{
	enum class State
	{
		Down, Up
	}

	private val arm = hardwareMap.servo.get("arm");
	var state: State = State.Up

	companion object
	{
		@JvmField
		var armDown: Double = 0.97;

		@JvmField
		var armUp: Double = 0.31;
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

	fun gotoPos(pos: Double)
	{
		arm.position = pos;
	}
}
