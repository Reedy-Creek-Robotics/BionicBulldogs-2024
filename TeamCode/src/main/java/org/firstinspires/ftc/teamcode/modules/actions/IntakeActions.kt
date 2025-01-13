package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

class IntakeAction_Intake: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.forward();
		return false;
	}
}

class IntakeAction_Outtake: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.reverse();
		return false;
	}
}

class IntakeAction_Stop: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.stop();
		return false;
	}
}

class IntakeAction_ZeroRotator(): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.zeroRotator();
		return false;
	}
}

class IntakeAction_SetRotation(private val rot: Double): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.setRotatorPos(rot);
		return false;
	}
}