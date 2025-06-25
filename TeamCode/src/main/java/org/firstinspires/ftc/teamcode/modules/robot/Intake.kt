package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime

@Config
class Intake(map: HardwareMap)
{

	enum class State
	{
		Forward, Stop, Reverse
	}

	var state: State = State.Stop;

	private val elapsedTime = ElapsedTime();
	private var targetTime = -1.0;

	//private var intake = map.crservo.get("rotator0");
	private var intake = map.dcMotor.get("intake");
	private var intakeRotator = map.servo.get("intakeRotator");

	companion object
	{
		@JvmField
		var spinPower: Double = 1.0;

		@JvmField
		var revPower: Double = 0.4;

		@JvmField
		var spinStop = 0.0;

		@JvmField
    var rotatorCenter = 0.26;

		@JvmField
    var rotatorIncrement = 0.25;

		@JvmField
		var autoRotatorIncrement = 0.29;
	}

  fun setRotatorPos(pos: Double)
  {
    intakeRotator.position = pos;
  }

  fun rotatorLeft()
  {
    intakeRotator.position += rotatorIncrement;
  }

  fun rotatorRight()
  {
    intakeRotator.position -= rotatorIncrement;
  }

  fun zeroRotator()
  {
    intakeRotator.position = rotatorCenter;
  }

	fun forward()
	{
		intake.power = -spinPower;
		state = State.Forward;
	}

	fun reverse(power: Double = 0.0)
	{
		intake.power = (if(power == 0.0) revPower else power);
		state = State.Reverse;
	}

	fun stop()
	{
		intake.power = spinStop;
		state = State.Stop;
	}

	/**
	 * @param time seconds
	 */
	fun stopIn(time: Double)
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
				stop();
				targetTime = -1.0;
			}
		}
	}
}
