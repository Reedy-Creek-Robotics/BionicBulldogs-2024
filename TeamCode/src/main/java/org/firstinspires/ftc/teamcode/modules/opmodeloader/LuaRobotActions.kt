package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.acmerobotics.roadrunner.Action
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.teamcode.modules.actions.*

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

			builder.addObjectFunction("hslideGotoPos", LuaType.Object(Action::class.java), listOf(LuaType.Double));
			builder.addObjectFunction("hslideZero", LuaType.Object(Action::class.java));
			builder.addObjectFunction("hslideGotoMin", LuaType.Object(Action::class.java));

			builder.addObjectFunction("intakeUp", LuaType.Object(Action::class.java));
			builder.addObjectFunction("intakeDown", LuaType.Object(Action::class.java));

			builder.addObjectFunction("intakeIntake", LuaType.Object(Action::class.java));
			builder.addObjectFunction("intakeOuttake", LuaType.Object(Action::class.java));
			builder.addObjectFunction("intakeStop", LuaType.Object(Action::class.java));

			builder.createClass("SpecimenOuttakeAction_Grab");
			builder.createClass("SpecimenOuttakeAction_GrabInstant");
			builder.createClass("SpecimenOuttakeAction_Score");
			builder.createClass("HSlideAction_GotoPos");
			builder.createClass("HSlideAction_GotoMin");
			builder.createClass("HSlideAction_Zero");
			builder.createClass("ArmAction_Up");
			builder.createClass("ArmAction_Down");
			builder.createClass("IntakeAction_Intake");
			builder.createClass("IntakeAction_Outtake");
			builder.createClass("IntakeAction_Stop");
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

	fun hslideGotoPos(pos: Double): Action
	{
		return HSlideAction_GotoPos(pos);
	}

	fun hslideGotoMin(): Action
	{
		return HSlideAction_GotoMin();
	}

	fun hslideZero(): Action
	{
		return HSlideAction_Zero();
	}

	fun intakeUp(): Action
	{
		return ArmAction_Up();
	}

	fun intakeDown(): Action
	{
		return ArmAction_Down();
	}

	fun intakeIntake(): Action
	{
		return IntakeAction_Intake();
	}

	fun intakeOuttake(): Action
	{
		return IntakeAction_Outtake();
	}

	fun intakeStop(): Action
	{
		return IntakeAction_Stop();
	}
}