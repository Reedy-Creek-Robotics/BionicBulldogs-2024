package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.acmerobotics.roadrunner.Action
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Grab
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Score

class LuaRobotActions
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.setCurrentObject(LuaRobotActions());
			builder.objectAddFun("specimenGrab", LuaType.Object(Action::class.java));
			builder.objectAddFun("specimenScore", LuaType.Object(Action::class.java));
		}
	}
	
	fun specimenGrab(): Action
	{
		return SpecimenOuttakeAction_Grab();
	}
	
	fun specimenScore(): Action
	{
		return SpecimenOuttakeAction_Score();
	}
}