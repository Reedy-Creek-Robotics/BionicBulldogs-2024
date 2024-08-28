package org.firstinspires.ftc.teamcode.opmode.lua

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.lua.LuaRoadRunner
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.modules.lua.TestModule

abstract class LuaAutoBase : LinearOpMode()
{
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		val drive = SampleMecanumDrive(hardwareMap);
		val luaRR = LuaRoadRunner(drive, this);
		val lua = luaRR.lua;
		
		val obj = TestModule(this);
		lua.addObject(obj);
		
		telemetry.addLine("initing lua");
		telemetry.update();
		
		val str = getOpmodeName();
		
		luaRR.init(str);
		
		telemetry.clearAll();
		telemetry.addLine("inited");
		telemetry.update();
		
		waitForStart();
		
		telemetry.clearAll();
		telemetry.update();
		
		luaRR.start(str);
	}
	
	abstract fun getOpmodeName(): String;
}