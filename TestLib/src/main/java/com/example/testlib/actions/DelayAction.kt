package com.example.testlib.actions

import com.example.testlib.timeNow

class DelayAction(private val delay: Double) : Action
{
	private var startTime: Long = 0;
	override fun start()
	{
		startTime = timeNow();
	}

	override fun update(): Boolean
	{
		return timeNow() - startTime > delay * 1000
	}
}