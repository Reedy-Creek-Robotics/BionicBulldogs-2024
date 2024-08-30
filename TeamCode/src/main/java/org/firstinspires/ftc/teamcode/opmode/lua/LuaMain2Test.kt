package org.firstinspires.ftc.teamcode.opmode.lua

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.lua.Lua
import org.firstinspires.ftc.teamcode.modules.lua.LuaType
import org.firstinspires.ftc.teamcode.modules.lua.TestModule

@Autonomous
class LuaMain2Test: LinearOpMode()
{
	override fun runOpMode()
	{
		val lua = Lua(this);
		lua.init();

		val builder = lua.getFunctionBuilder();

		val servos = TestModule(this);

		builder.setCurrentObject(servos);

		builder.newClass();
		builder.addFun("setPos", LuaType.Void, listOf(LuaType.Double));
		builder.addFun("setPos2", LuaType.Void, listOf(LuaType.Double));
		builder.endClass("servos");

		waitForStart();

		lua.startLoop("main2");
	}
}