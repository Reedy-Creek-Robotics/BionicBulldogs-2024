package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.opmode.telop.OuttakeTelop.Companion.slidePosition

@Config
class SampleClawAction_Transfer : Action
{
	companion object
	{
		@JvmField
		var intakeArmCollect = 0.75;
	}

	private var state = 1;
	private var elapsedTime = ElapsedTime();
	override fun run(p: TelemetryPacket): Boolean
	{
		when(state)
		{
			1 ->
			{
				arm.gotoPos(intakeArmCollect);
				elapsedTime.reset();
				state = 2;
			}

			2 ->
			{
				if(elapsedTime.seconds() >= 0.3)
				{
					sampleClaw.open();
					sampleClaw.armGrab();
					sampleClaw.rotatorDown();
					elapsedTime.reset();
					state = 3;
				}
			}

			3 ->
			{
				if(elapsedTime.seconds() >= 0.2)
				{
					sampleClaw.close();
					elapsedTime.reset();
					state = 4;
				}
			}

			4 ->
			{
				if(elapsedTime.seconds() >= 0.1)
				{
					intake.reverse(1.0);
					elapsedTime.reset();
					state = 5;
				}
			}

			5 ->
			{
				if(elapsedTime.seconds() >= 0.2)
				{
					sampleClaw.armStart();
					elapsedTime.reset();
					state = 6;
				}
			}

			6 ->
			{
				if(elapsedTime.seconds() >= 0.1)
				{
					intake.stop();
					return false;
				}
			}
		}
		return true;
	}
}

class SampleClawAction_Up: Action
{
	private var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			slide.gotoPos(slidePosition);
			ran = true;
		}

		if(slide.getPos() < -500)
		{
			sampleClaw.rotatorUp();
			sampleClaw.armUp();
			return false;
		}
		return true;
	}
}

class SampleClawAction_Score: Action
{
	private var ran = false;
	private val elapsedTime = ElapsedTime();
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			sampleClaw.open();
			elapsedTime.reset()
			ran = true;
		}

		if(elapsedTime.seconds() >= 0.2)
		{
			sampleClaw.rotatorDown();
			sampleClaw.armStart();
			slide.lower();
			return false;
		}
		return true;
	}
}
