package org.firstinspires.ftc.teamcode.opmode.auto

import org.firstinspires.ftc.teamcode.opmode.auto.Actions.Action
import java.util.Timer

class DelayAction(private val delay: Double) : Action
{
	val elapsedTime = Ela;
	override fun start()
	{
	}

	override fun update(): Boolean
	{
		return elapsedTime.seconds() > delay;
	}
}