package org.firstinspires.ftc.teamcode.opmode.lua

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.lua.Lua
import org.firstinspires.ftc.teamcode.modules.lua.LuaError
import org.firstinspires.ftc.teamcode.modules.lua.TestModule
import java.lang.Exception

abstract class LuaAutoBase : LinearOpMode()
{
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		val lua = Lua(this);
		
		val obj = TestModule(this);
		lua.addObject(obj);
		
		telemetry.addLine("initing lua");
		telemetry.update();
		
		lua.init();
		
		val str = getOpmodeName();
		
		lua.initRR(str);
		
		telemetry.clearAll();
		telemetry.addLine("inited");
		telemetry.update();
		
		waitForStart();
		
		telemetry.clearAll();
		telemetry.update();
		
		lua.startRR(str);
	}
	
	abstract fun getOpmodeName(): String;
}