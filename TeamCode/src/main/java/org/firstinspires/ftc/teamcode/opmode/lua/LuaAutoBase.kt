package org.firstinspires.ftc.teamcode.opmode.lua

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.TestModule
import org.firstinspires.ftc.teamcode.opmodeloader.LuaType
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.opmodeloader.OpmodeLoaderRR

abstract class LuaAutoBase : LinearOpMode()
{
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		val drive = SampleMecanumDrive(hardwareMap);
		val luaRR = OpmodeLoaderRR(drive, this);
		
		telemetry.addLine("initing lua");
		telemetry.update();
		
		luaRR.init();
		
		val obj = TestModule(hardwareMap);
		
		val builder = luaRR.getFunctionBuilder();
		
		builder.setCurrentObject(obj);
		
		builder.newClass();
		builder.addFun("setPos", LuaType.Void, listOf(LuaType.Double));
		builder.addFun("setPos2", LuaType.Void, listOf(LuaType.Double));
		builder.endClass("servos");
		
		val str = getOpmodeName();
		
		luaRR.loadOpmode(str);
		
		telemetry.clearAll();
		telemetry.addLine("inited");
		telemetry.update();
		
		waitForStart();
		
		telemetry.clearAll();
		telemetry.update();
		
		luaRR.start(1);
	}
	
	abstract fun getOpmodeName(): String;
}