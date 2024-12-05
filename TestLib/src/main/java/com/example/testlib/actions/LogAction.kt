package com.example.testlib.actions

import com.example.testlib.startTime
import com.example.testlib.timeNow

class LogAction(private val str: String) : Action
{
	override fun start()
	{
		println("$str, ${timeNow() - startTime}");
	}

	override fun update(): Boolean
	{
		return true;
	}
}