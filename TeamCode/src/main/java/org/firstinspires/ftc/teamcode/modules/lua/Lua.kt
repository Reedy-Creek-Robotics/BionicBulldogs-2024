package org.firstinspires.ftc.teamcode.modules.lua

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class Lua
{
	companion object
	{
		init
		{
			System.loadLibrary("ftcrobotcontroller");
		}
	}
	
	private val opmode: LinearOpMode;
	
	constructor(opmode2: LinearOpMode)
	{
		opmode = opmode2;
	}
	
	fun init(): Array<String>
	{
		return internalInit(LuaStdlib(opmode));
	}
	
	private external fun internalInit(luaStdlib: LuaStdlib): Array<String>;
	external fun <T> addObject(thing: T);
	external fun start(name: String, recognition: Int);
	external fun stop();
	external fun update(delatTime: Float, elapsedTime: Float);
}