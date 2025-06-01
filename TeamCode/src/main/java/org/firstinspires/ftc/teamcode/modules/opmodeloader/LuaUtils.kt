package org.firstinspires.ftc.teamcode.modules.opmodeloader;

import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class LuaUtils(private val opMode: LinearOpMode)
{
	companion object
	{
		fun init(builder: FunctionBuilder, opMode: LinearOpMode)
		{
			LuaTelem.init(builder);
			builder.setCurrentObject(LuaUtils(opMode));
			builder.addObjectFunction("getTelem", LuaType.Object(LuaTelem::class.java));
		}
	}

	fun getTelem(): LuaTelem
	{
		return LuaTelem(opMode.telemetry);
	}
}