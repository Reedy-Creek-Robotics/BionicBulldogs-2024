package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.util.ElapsedTime
@Config
class SpecimenOuttake(private val claw: SpeciminClaw, private val slide: Slide)
{
	companion object
	{
		@JvmField
		var relesePos = -1000;
	}

	private val elapsedTime = ElapsedTime();

	var state = State.Down;

	enum class State
	{
		Up, Down, Lowering, Raising
	}

	fun init()
	{
		claw.open();
	}

	fun score()
	{
		slide.lower();
		state = State.Lowering;
	}

	fun collect()
	{
		elapsedTime.reset();
		claw.close();
		state = State.Raising;
	}

	fun update()
	{
		if(state == State.Raising)
		{
			if(elapsedTime.seconds() > 0.5)
			{
				slide.raise();
				state = State.Up;
			}
		}
		if(state == State.Lowering)
		{
			if(slide.getPos() > relesePos)
			{
				claw.open();
				state = State.Down;
			}
		}
	}

	fun waitUntilIdle()
	{
		while(state == State.Raising || state == State.Lowering)
		{
			update();
		}
	}
}