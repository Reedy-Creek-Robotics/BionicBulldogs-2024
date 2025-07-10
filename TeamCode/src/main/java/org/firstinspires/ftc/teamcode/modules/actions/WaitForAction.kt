package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

class WaitForOtherAction(private val mainAction: Action, private val secondaryAction: Action): Action
{
	private var runOther = true;
	override tailrec fun run(p: TelemetryPacket): Boolean
	{
		if(runOther)
			runOther = secondaryAction.run(p);
		return mainAction.run(p);
	}
}