package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime

@Config
class Intake(private val spin0: CRServo)
{
	enum class State
	{
		Forward, Stop, Reverse
	}

	var state: State = State.Stop;

	private val elapsedTime = ElapsedTime();
	private var targetTime = -1.0;

	companion object
	{
		@JvmField
		var spinPower: Double = 1.0;

		@JvmField
		var spinStop = 0.0;
	}

	fun forward()
	{
		spin0.power = -spinPower;
		state = State.Forward;
	}

	fun reverse()
	{
		spin0.power = spinPower;
		state = State.Reverse;
	}

	fun stop()
	{
		spin0.power = spinStop;
		state = State.Stop;
	}

	/**
	 * @param time seconds
	 */
	fun outtakeForTime(time: Double)
	{
		elapsedTime.reset();
		targetTime = time;
		reverse();
	}

	fun update()
	{
		if(targetTime > 0)
		{
			if(targetTime < elapsedTime.seconds())
			{
				forward();
				targetTime = -1.0;
			}
		}
	}
}
