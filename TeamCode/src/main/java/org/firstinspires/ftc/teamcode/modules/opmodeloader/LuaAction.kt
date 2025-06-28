package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaError
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.teamcode.modules.actions.drive
import org.firstinspires.ftc.teamcode.modules.actions.profiler.MarkerAction
import org.firstinspires.ftc.teamcode.modules.actions.profiler.MarkerSequentialAction
import org.firstinspires.ftc.teamcode.modules.actions.profiler.ProfileSequentialAction
import org.firstinspires.ftc.teamcode.modules.actions.profiler.toProfileAction
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import java.io.File
import java.io.FileWriter
import kotlin.math.PI

class LuaAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			MecanumDrive.PARAMS.maxWheelVel = 80.0;
			MecanumDrive.PARAMS.maxProfileAccel = 60.0;
			MecanumDrive.PARAMS.maxAngVel = PI * 4.0;
			MecanumDrive.PARAMS.maxAngAccel = PI * 4.0;

			builder.setCurrentObject(LuaAction());

			builder.addObjectFunction("run", LuaType.Void, listOf(LuaType.Object(Action::class.java)));
			builder.addObjectFunction(
				"runTimer",
				LuaType.Void,
				listOf(LuaType.Object(Action::class.java), LuaType.String)
			);
			builder.addObjectFunction(
				"trajectoryAction", LuaType.Object(LuaTrajectoryBuilder::class.java), listOf(
					LuaType.Double, LuaType.Double, LuaType.Double
				)
			);

			builder.addObjectFunction(
				"trajectoryActionX", LuaType.Object(LuaTrajectoryBuilder::class.java), listOf(
					LuaType.Double, LuaType.Double, LuaType.Double,
					LuaType.Double, LuaType.Double, LuaType.Double
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
				"markerSequentialAction",
				LuaType.Object(LuaSequentalAction::class.java),
				listOf(LuaType.String)
			);

			builder.addObjectFunction(
				"parallelAction", LuaType.Object(LuaParallelAction::class.java)
			);

			builder.addObjectFunction(
				"sleepAction",
				LuaType.Object(Action::class.java),
				listOf(LuaType.Double)
			);

			builder.addObjectFunction(
				"markerAction",
				LuaType.Object(Action::class.java),
				listOf(LuaType.String)
			);

			builder.addObjectFunction(
				"setAngAccel",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double)
			);

			builder.addObjectFunction(
				"initProfileAction",
				LuaType.Object(Action::class.java),
				listOf(LuaType.Object(Action::class.java))
			);

			builder.createClass(Action::class.java.simpleName);
			builder.createClass(SequentialAction::class.java.simpleName);
			builder.createClass(ParallelAction::class.java.simpleName);
			builder.createClass(MarkerSequentialAction::class.java.simpleName);
			builder.createClass(ProfileSequentialAction::class.simpleName!!);
			builder.createClass(SleepAction::class.java.simpleName);
			builder.createClass(MarkerAction::class.java.simpleName);

			LuaTrajectoryBuilder.init(builder);
			LuaSequentalAction.init(builder);
			LuaParallelAction.init(builder);
		}
	}

	fun setPosEstimate(x: Double, y: Double, h: Double)
	{
		drive.localizer.pose = Pose2d(x, y, Math.toRadians(h));
	}

	fun setAngAccel(v: Double, a: Double)
	{
		MecanumDrive.PARAMS.maxAngVel = v;
		MecanumDrive.PARAMS.maxAngAccel = a;
	}

	fun trajectoryAction(x: Double, y: Double, h: Double): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(x, y, h);
	}

	fun trajectoryActionX(
		x: Double, y: Double, h: Double, vel: Double, minAccel: Double, maxAccel: Double
	): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(x, y, h, vel, minAccel, maxAccel);
	}

	fun sequentalAction(): LuaSequentalAction
	{
		return LuaSequentalAction(null);
	}

	fun parallelAction(): LuaParallelAction
	{
		return LuaParallelAction();
	}

	fun markerSequentialAction(label: String): LuaSequentalAction
	{
		return LuaSequentalAction(label);
	}

	fun sleepAction(time: Double): Action
	{
		return SleepAction(time);
	}

	fun markerAction(name: String): Action
	{
		return MarkerAction(name);
	}

	fun runTimer(action: Action, filename: String)
	{
		val a2 =
			if(action is ProfileSequentialAction) action else toProfileAction(action as SequentialAction);
		runBlocking(a2);
		val file = File("/sdcard/$filename");
		if(!file.exists())
			file.createNewFile();
		val writer = FileWriter(file);
		writer.write(a2.timerString());
		writer.close();
		rotPos = drive.localizer.pose.heading.toDouble();
	}

	fun run(action: Action)
	{
		runBlocking(action);
		rotPos = drive.localizer.pose.heading.toDouble();
	}

	fun initProfileAction(action: Action): Action
	{
		if(action !is SequentialAction)
			throw LuaError("action must be a sequential action");
		return toProfileAction(action);
	}
}

class LuaSequentalAction(val label: String?)
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.addClassFunction(
				LuaSequentalAction::class.java,
				"add",
				LuaType.Builder,
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
		return if(label == null)
			SequentialAction(actions);
		else
			MarkerSequentialAction(label, actions);
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
				LuaType.Builder,
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