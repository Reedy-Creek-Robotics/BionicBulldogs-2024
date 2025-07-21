package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry

@Config
class SpecimenOuttake(private val claw: SpeciminClaw, private val slide: Slide)
{
	companion object
	{
		@JvmField
		var relesePosHigh = -1040;

		@JvmField
		var relesePosLow = -1;

		@JvmField
		var pause = 0.12;
	}

	private val elapsedTime = ElapsedTime();
	private var targetPos = Target.High;

	var state = State.Down;


	enum class Target
	{
		Low, High
	}

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

	fun collectLow()
	{
		elapsedTime.reset();
		claw.close();
		state = State.Raising;
		targetPos = Target.Low;
	}

	fun collectHigh()
	{
		elapsedTime.reset();
		claw.close();
		state = State.Raising;
		targetPos = Target.High;
	}

	fun collectInstant()
	{
		claw.close();
		state = State.Raising;
		targetPos = Target.High;
		update();
	}

	fun update()
	{
		if(state == State.Raising)
		{
			if(elapsedTime.seconds() > pause)
			{
				if(targetPos == Target.Low)
					slide.gotoLow();
				else
					slide.gotoHigh();
				state = State.Up;
			}
		}
		if(state == State.Lowering)
		{
			val target = if(targetPos == Target.Low) relesePosLow else relesePosHigh;
			if(slide.getPos() > target || slide.state == Slide.State.Stalled)
			{
				claw.open();
				state = State.Down;
				slide.state = Slide.State.Down;
			}
		}
	}

	fun waitUntilIdle()
	{
		while(isBusy())
		{
			update();
		}
	}

	fun isBusy(): Boolean
	{
		return state == State.Raising || state == State.Lowering;
	}

	fun telem(t: Telemetry)
	{
		t.addData("SpecimenOuttake: state", state);
		t.addData("SpecimenOuttake: clawState", claw.state);
	}
}
