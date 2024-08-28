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
	
	private val opmode = opmode2;
	private val stdlib = LuaStdlib(opmode2);
	
	/**
	 * creates and returns a function builder object for exposing functions to lua
	 */
	fun getFunctionBuilder(): LuaFunctionBuilder
	{
		val builder = LuaFunctionBuilder();
		builder.setCurrentObject(stdlib);
		
		builder.addFun("print", LuaType.Void, listOf(LuaType.String));
		builder.addFun("delay", LuaType.CheckRun, listOf(LuaType.Float));
		builder.addFun("checkRunning", LuaType.CheckRun, listOf(LuaType.Void));
		
		builder.newClass();
		builder.addFun("addData", LuaType.Void, listOf(LuaType.String, LuaType.String));
		builder.addFun("update", LuaType.Void, listOf());
		builder.endClass("telem");
		
		return builder;
	}
	
	fun init(): Array<String>
	{
		return internalInit(LuaStdlib(opmode));
	}
	
	fun start(name: String)
	{
		start(name, defaultRecognition);
	}
	
	private external fun internalInit(luaStdlib: LuaStdlib): Array<String>;
	external fun start(name: String, recognition: Int);
	external fun stop();
	external fun update(delatTime: Float, elapsedTime: Float);
}