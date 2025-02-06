package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.acmerobotics.roadrunner.Action
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Grab
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_GrabInstant
import org.firstinspires.ftc.teamcode.modules.actions.SpecimenOuttakeAction_Score

class LuaRobotActions
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.setCurrentObject(LuaRobotActions());
			builder.addObjectFunction("specimenGrab", LuaType.Object(Action::class.java));
			builder.addObjectFunction("specimenGrabInstant", LuaType.Object(Action::class.java));
			builder.addObjectFunction("specimenScore", LuaType.Object(Action::class.java));
			builder.createClass("SpecimenOuttakeAction_Grab");
			builder.createClass("SpecimenOuttakeAction_GrabInstant");
			builder.createClass("SpecimenOuttakeAction_Score");
		}
	}
	
	fun specimenGrab(): Action
	{
		return SpecimenOuttakeAction_Grab();
	}

	fun specimenGrabInstant(): Action
	{
		return SpecimenOuttakeAction_GrabInstant();
	}

	fun specimenScore(): Action
	{
		return SpecimenOuttakeAction_Score();
	}
}