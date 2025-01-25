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
				"setPosEstimate", LuaType.Void(), listOf(
					LuaType.Double(), LuaType.Double(), LuaType.Double()
				)
			);
			
			builder.objectAddFun(
				"sequentalAction", LuaType.Object(LuaSequentalAction::class.java)
			);
			
			builder.objectAddFun(
				"parallelAction", LuaType.Object(LuaParallelAction::class.java)
			);
			
			builder.objectAddFun(
				"sleepAction",
				LuaType.Object(Action::class.java),
				listOf(LuaType.Double())
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
		drive.localizer.pose = Pose2d(x, y, Math.toRadians(h));
	}
	
	fun trajectoryAction(x: Double, y: Double, h: Double): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(drive.actionBuilder(Pose2d(x, y, Math.toRadians(h))));
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
		if(action is SequentialAction)
		{
			for(a in action.initialActions)
			{
				Log.d("running action", a.javaClass.simpleName);
				if(a is SequentialAction)
				{
					for(a2 in action.initialActions)
					{
						Log.d("running action2", a2.javaClass.simpleName);
					}
				}
			}
		}
		//runBlocking(action);
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