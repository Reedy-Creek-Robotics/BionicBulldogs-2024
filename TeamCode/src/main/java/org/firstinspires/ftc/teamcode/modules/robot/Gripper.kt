package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo

@Config
class Gripper(private val gripper: Servo)
{

	enum class State
	{
		Close, Open
	}

	var state = State.Close;

	companion object
	{
		@JvmField
		var gripClose: Double = 0.9; // 0.07

		@JvmField
		var gripOpen: Double = 0.53; // 0.17
	}

	fun open()
	{
		gripper.position = gripOpen;
		state = State.Open;
	}

	fun close()
	{
		gripper.position = gripClose;
		state = State.Close;
	}
}