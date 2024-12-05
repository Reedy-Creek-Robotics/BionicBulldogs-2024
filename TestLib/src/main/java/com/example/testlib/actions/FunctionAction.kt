package com.example.testlib.actions

fun interface StartFun
{
	fun start();
}

fun interface UpdateFun
{
	fun update(): Boolean;
}

class FunctionAction(private val startFun: StartFun? = null, private val updateFun: UpdateFun? = null) :
	Action
{
	override fun start()
	{
		startFun?.start();
	}

	override fun update(): Boolean
	{
		return updateFun?.update() ?: true;
	}
}