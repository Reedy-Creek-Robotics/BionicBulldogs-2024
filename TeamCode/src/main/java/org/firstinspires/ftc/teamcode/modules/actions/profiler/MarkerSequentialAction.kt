package org.firstinspires.ftc.teamcode.modules.actions.profiler

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

data class MarkerSequentialAction(
	val label: String,
	val initialActions: List<Action>
) : Action
{
	private var actions = initialActions

	constructor(label: String, vararg actions: Action) : this(label, actions.asList())

	override tailrec fun run(p: TelemetryPacket): Boolean {
		if (actions.isEmpty()) {
			return false
		}

		return if (actions.first().run(p)) {
			true
		} else {
			actions = actions.drop(1)
			run(p)
		}
	}
}
