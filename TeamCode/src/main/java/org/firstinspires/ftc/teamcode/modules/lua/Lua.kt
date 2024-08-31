package org.firstinspires.ftc.teamcode.modules.lua

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.acmerobotics.dashboard.config.Config

@Config
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
	private val builder = LuaFunctionBuilder();
	
	/**
	 * creates and returns a function builder object for exposing functions to lua
	 */
	fun getFunctionBuilder(): LuaFunctionBuilder
	{
		return builder;
	}
	
	fun init(): Array<String>
	{
		val opmodes = internalInit(stdlib);

		builder.setCurrentObject(stdlib);

		builder.addFun("print", LuaType.Void, listOf(LuaType.String));
		builder.addFun("delay", LuaType.CheckRun, listOf(LuaType.Double));
		builder.addFun("checkRunning", LuaType.CheckRun);
		builder.addFun("stop", LuaType.CheckRun);

		builder.newClass();
		builder.addFun("addData", LuaType.Void, listOf(LuaType.String, LuaType.String));
		builder.addFun("update", LuaType.Void);
		builder.endClass("telem");

		return opmodes;
	}
	
	fun start(name: String)
	{
		start(name, defaultRecognition);
	}

	fun startLoop(name: String)
	{
		startLoop(name, defaultRecognition);
	}

	fun startLoop(name: String, recognition: Int)
	{
		start(name, recognition)

		val elapsedTime = ElapsedTime();
		elapsedTime.reset();
		var prevTime = 0.0;

		while(opmode.opModeIsActive())
		{
			val curTime = elapsedTime.seconds();
			val deltaTime = curTime - prevTime;
			if(update(deltaTime, curTime))
				return;
			prevTime = curTime;
		}
	}
	
	private external fun internalInit(luaStdlib: LuaStdlib): Array<String>;
	external fun start(name: String, recognition: Int);
	external fun stop();
	external fun update(deltaTime: Double, elapsedTime: Double): Boolean;
}