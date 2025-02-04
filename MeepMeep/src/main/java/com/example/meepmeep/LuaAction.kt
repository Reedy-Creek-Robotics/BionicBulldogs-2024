package com.example.meepmeep

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType

class LuaAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.setCurrentObject(LuaAction());

			builder.addObjectFunction("run", LuaType.Void, listOf(LuaType.Object(Action::class.java)));
			builder.addObjectFunction("runTimer", LuaType.Void, listOf(LuaType.Object(Action::class.java)));
			builder.addObjectFunction("print", LuaType.Void, listOf(LuaType.String));
			builder.addObjectFunction(
				"trajectoryAction", LuaType.Object(LuaTrajectoryBuilder::class.java), listOf(
					LuaType.Double, LuaType.Double, LuaType.Double
				)
			);

			builder.addObjectFunction(
				"trajectoryActionX", LuaType.Object(LuaTrajectoryBuilder::class.java), listOf(
					LuaType.Double,
					LuaType.Double,
					LuaType.Double,
					LuaType.Double,
					LuaType.Double,
					LuaType.Double
				)
			);

			builder.addObjectFunction(
				"setPosEstimate", LuaType.Void, listOf(
					LuaType.Double, LuaType.Double, LuaType.Double
				)
			);

			builder.addObjectFunction(
				"sequentalAction", LuaType.Object(LuaSequentalAction::class.java)
			);

			builder.addObjectFunction(
				"parallelAction", LuaType.Object(LuaParallelAction::class.java)
			);

			builder.addObjectFunction(
				"sleepAction", LuaType.Object(Action::class.java), listOf(LuaType.Double)
			);

			builder.createClass("Action");
			builder.createClass("SequentialAction");
			builder.createClass("ParallelAction");
			builder.createClass("SleepAction");

			LuaTrajectoryBuilder.init(builder);
			LuaSequentalAction.init(builder);
			LuaParallelAction.init(builder);
		}
	}

	fun setPosEstimate(x: Double, y: Double, h: Double)
	{
	}

	fun trajectoryAction(x: Double, y: Double, h: Double): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(drive!!.actionBuilder(Pose2d(x, y, Math.toRadians(h))));
	}

	fun trajectoryActionX(
		x: Double, y: Double, h: Double, vel: Double, minAccel: Double, maxAccel: Double
	): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(drive!!.actionBuilder(Pose2d(x, y, Math.toRadians(h))));
	}

	fun sequentalAction(): LuaSequentalAction
	{
		return LuaSequentalAction();
	}

	fun parallelAction(): LuaParallelAction
	{
		return LuaParallelAction();
	}

	fun sleepAction(time: Double): Action
	{
		return SleepAction(time);
	}

	fun run(a3: Action)
	{
		action = a3;
	}
	
	fun runTimer(a3: Action)
	{
		action = a3;
		val a2 = toTimerAction(a3 as SequentialAction);
		val p = TelemetryPacket();
		while(a2.run(p));
		kotlin.io.print(a2.timerString());
	}

	fun print(str: String)
	{
		println("lua: $str");
	}
}

class LuaSequentalAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.addClassFunction(
				LuaSequentalAction::class.java,
				"add",
				LuaType.Void,
				listOf(LuaType.Object(Action::class.java))
			);
			builder.addClassFunction(
				LuaSequentalAction::class.java, "build", LuaType.Object(Action::class.java)
			);
		}
	}

	private val actions = ArrayList<Action>();

	fun add(action: Action)
	{
		actions.add(action)
	}

	fun build(): Action
	{
		return SequentialAction(actions);
	}
}

class LuaParallelAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.addClassFunction(
				LuaParallelAction::class.java,
				"add",
				LuaType.Void,
				listOf(LuaType.Object(Action::class.java))
			);
			builder.addClassFunction(
				LuaParallelAction::class.java, "build", LuaType.Object(Action::class.java)
			);
		}
	}

	private val actions = ArrayList<Action>();

	fun add(action: Action)
	{
		actions.add(action)
	}

	fun build(): Action
	{
		return ParallelAction(actions);
	}
}
