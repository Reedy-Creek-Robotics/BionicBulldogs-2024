package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

class ArmAction_Up: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		arm.up();
		return false;
	}
}

class ArmAction_Down: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		arm.down();
		return false;
	}
}
