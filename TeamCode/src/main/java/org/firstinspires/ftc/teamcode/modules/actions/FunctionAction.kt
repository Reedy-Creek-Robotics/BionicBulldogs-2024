package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.util.ElapsedTime

fun interface FunctionActionCallback
{
	fun callback(elapsedTime: Float): Boolean
}

class FunctionAction(val callback: FunctionActionCallback): Action
{
	private val e = ElapsedTime();
	private var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			e.reset();
			ran = true;
		}
		return callback.callback(e.seconds().toFloat());
	}
}