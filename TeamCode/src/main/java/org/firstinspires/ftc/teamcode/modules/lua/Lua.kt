package org.firstinspires.ftc.teamcode.modules.lua

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class Lua(opmode2: LinearOpMode)
{
	companion object
	{
		init
		{
			System.loadLibrary("ftcrobotcontroller");
		}
		@JvmStatic
		val defaultRecognition: Int = 0;
	}
	
	private val opmode: LinearOpMode = opmode2;

	fun init(): Array<String>
	{
		return internalInit(LuaStdlib(opmode));
	}

	fun start(name: String)
	{
		start(name, defaultRecognition);
	}
	
	private external fun internalInit(luaStdlib: LuaStdlib): Array<String>;
	external fun <T> addObject(thing: T);
	external fun <T> addFunction(thing: T, name: String, signature: String);
	external fun start(name: String, recognition: Int);
	external fun stop();
	external fun update(delatTime: Float, elapsedTime: Float);
}