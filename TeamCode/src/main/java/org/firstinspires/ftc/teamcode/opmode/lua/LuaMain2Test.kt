package org.firstinspires.ftc.teamcode.opmode.lua

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.TestModule
import org.firstinspires.ftc.teamcode.opmodeloader.LuaType
import org.firstinspires.ftc.teamcode.opmodeloader.OpmodeLoader

@Autonomous
class LuaMain2Test: LinearOpMode()
{
	override fun runOpMode()
	{
		val lua = OpmodeLoader(this);
		lua.init();

		val builder = lua.getFunctionBuilder();

		val servos = TestModule(hardwareMap);

		builder.setCurrentObject(servos);

		builder.newClass();
		builder.addFun("setPos", LuaType.Void, listOf(LuaType.Double));
		builder.addFun("setPos2", LuaType.Void, listOf(LuaType.Double));
		builder.endClass("servos");

		lua.loadOpmode("main2");
		
		waitForStart();

		lua.startLoop();
	}
}