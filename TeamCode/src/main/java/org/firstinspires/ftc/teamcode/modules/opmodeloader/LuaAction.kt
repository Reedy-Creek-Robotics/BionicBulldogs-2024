package org.firstinspires.ftc.teamcode.modules.opmodeloader

import android.util.Log
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.teamcode.modules.actions.drive

class LuaAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.setCurrentObject(LuaAction());
			
			builder.objectAddFun("run", LuaType.Void(), listOf(LuaType.Object(Action::class.java)));
			builder.objectAddFun("print", LuaType.Void(), listOf(LuaType.String()));
			builder.objectAddFun(
				"trajectoryAction", LuaType.Object(LuaTrajectoryBuilder::class.java), listOf(
					LuaType.Double(), LuaType.Double(), LuaType.Double()
				)
			);
			
			builder.objectAddFun(
				"sequentalAction", LuaType.Object(LuaSequentalAction::class.java)
			);
			
			builder.objectAddFun(
				"parallelAction", LuaType.Object(LuaTrajectoryBuilder::class.java)
			);
			
			builder.objectAddFun(
				"sleepAction",
				LuaType.Object(Action::class.java),
				listOf(LuaType.Double())
			);
			
			builder.createClass("Action");
			
			LuaTrajectoryBuilder.init(builder);
		}
	}
	
	fun trajectoryAction(x: Double, y: Double, h: Double): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(drive.actionBuilder(Pose2d(x, y, h)));
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
	
	fun run(action: Action)
	{
		runBlocking(action);
	}
	
	fun print(str: String)
	{
		Log.d("lua", str);
	}
}

class LuaSequentalAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.classAddFun(
				LuaSequentalAction::class.java,
				"add",
				LuaType.Void(),
				listOf(LuaType.Object(Action::class.java))
			);
			builder.classAddFun(
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
			builder.classAddFun(
				LuaParallelAction::class.java,
				"add",
				LuaType.Void(),
				listOf(LuaType.Object(Action::class.java))
			);
			builder.classAddFun(
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