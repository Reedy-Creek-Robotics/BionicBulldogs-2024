package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime

@Config
class Outtake(val hardwareMap: HardwareMap)
{
	companion object
	{
		@JvmField
		var armUp = 0.0;

		@JvmField
		var armDown = 0.95;

		@JvmField
		var bucketUp = 0.45;

		@JvmField
		var bucketDown = 0.85;

		@JvmField
		var bucketScore = 1.0;

		@JvmField
		var bucketUpTime = 0.3;

		@JvmField
		var parkPos = 0.1;
	}

	enum class State
	{
		Up, Score, Idle, Score2
	}

	var state = State.Idle;

	private val arm: Servo = hardwareMap.servo.get("outtakeArm");
	private val bucket: Servo = hardwareMap.servo.get("bucket");

	private val elapsedTime = ElapsedTime();

	fun up()
	{
		elapsedTime.reset();
		arm.position = armUp;
		state = State.Up;
	}

	fun score2()
	{
		elapsedTime.reset();
		arm.position = armUp;
		state = State.Score2;
	}

	fun score()
	{
		elapsedTime.reset();
		bucket.position = bucketScore;
		state = State.Score;
	}

	fun update(): Int
	{
		if(state != State.Idle)
		{
			if(state == State.Up || state == State.Score2)
			{
				if(elapsedTime.seconds() > bucketUpTime)
				{
					bucket.position = bucketUp;
					if(state == State.Score2)
						score();
					else
						state = State.Idle;
				}
			}
			if(state == State.Score)
			{
				if(elapsedTime.seconds() > 0.5)
				{
					bucket.position = bucketDown;
					arm.position = armDown;
					state = State.Idle;
					return 1;
				}
			}
		}
		return 0;
	}

	fun armDown()
	{
		arm.position = armDown;
	}

	fun armUp()
	{
		arm.position = armUp;
	}

	fun bucketDown()
	{
		bucket.position = bucketDown;
	}

	fun bucketUp()
	{
		bucket.position = bucketUp;
	}

	fun bucketScore()
	{
		bucket.position = bucketScore;
	}

	fun park()
	{
		arm.position = parkPos;
	}

	fun isArmUp(): Boolean
	{
		return arm.position == armUp;
	}
}
