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
		var armUp = 0.5;

		@JvmField
		var armDown = 0.03;

		@JvmField
		var bucketUp = 0.6;

		@JvmField
		var bucketDown = 0.9;

		@JvmField
		var bucketScore = 1.0;
	}

	enum class State
	{
		Up, Score, Idle
	}

	private var state = State.Idle;

	private val arm: Servo = hardwareMap.servo.get("outtakeArm");
	private val bucket: Servo = hardwareMap.servo.get("bucket");

	private val elapsedTime = ElapsedTime();

	fun up()
	{
		elapsedTime.reset();
		arm.position = armUp;
		state = State.Up;
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
			if(state == State.Up)
			{
				if(elapsedTime.seconds() > 0.5)
				{
					bucket.position = bucketUp;
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
}