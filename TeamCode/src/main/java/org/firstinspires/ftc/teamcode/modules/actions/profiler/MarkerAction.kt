package org.firstinspires.ftc.teamcode.modules.actions.profiler

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action


data class MarkerAction(private val label: String): Action
{
	fun getLabel(): String
	{
		return "MarkerAction: $label";
	}

	override fun run(p: TelemetryPacket): Boolean
	{
		return false;
	}
}
