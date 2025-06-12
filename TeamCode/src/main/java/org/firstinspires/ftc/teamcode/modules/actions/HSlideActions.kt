package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.modules.robot.HSlide

class HSlideAction_Zero: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		hSlide.zero();
		return false;
	}
}

class HSlideAction_Score: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		hSlide.score();
		return false;
	}
}

class HSlideAction_GotoPos(private val pos: Double): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		hSlide.gotoPos(pos);
		return false;
	}
}

class HSlideAction_GotoMin: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		hSlide.gotoPos(HSlide.min);
		return false;
	}
}
