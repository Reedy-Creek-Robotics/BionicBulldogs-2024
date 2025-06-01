package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.robotcore.external.Telemetry

class LuaTelem(val telem: Telemetry)
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.addClassFunction(
				LuaTelem::class.java,
				"addDatas",
				LuaType.Void,
				listOf(LuaType.String, LuaType.String)
			);
			builder.addClassFunction(
				LuaTelem::class.java,
				"addDatad",
				LuaType.Void,
				listOf(LuaType.String, LuaType.Double)
			);
			builder.addClassFunction(
				LuaTelem::class.java,
				"addLine",
				LuaType.Void,
				listOf(LuaType.String)
			);
			builder.addClassFunction(
				LuaTelem::class.java,
				"update"
			);
		}
	}

	fun addDatas(name: String, value: String)
	{
		telem.addData(name, value);
	}

	fun addDatad(name: String, value: Double)
	{
		telem.addData(name, value);
	}

	fun addLine(name: String)
	{
		telem.addLine(name);
	}

	fun update()
	{
		telem.update();
	}
}