package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import org.firstinspires.ftc.teamcode.modules.format

data class TimerSequentialAction(
	val initialActions: List<Action>
): Action
{
	private var actions = initialActions;

	private val times = ArrayList<Long>();
	private var startTime: Long = 0L;
	private var startTime2: Long = 0L;
	private var elapsedTime: Long = 0L;

	constructor(vararg actions: Action): this(actions.asList());

	fun timerString(level: Int = 0, lines: Int = 0): String
	{
		var out = "";
		if(times.size != initialActions.size && level == 0)
			out += "Timing is not complete, some information is not available\n";
		if(elapsedTime == 0L)
			elapsedTime = 1L;
		if(level == 0) out += "${javaClass.simpleName} - ${elapsedTime.toDouble() / 1000}\n";

		for((i, a) in initialActions.withIndex())
		{
			for(i2 in 0 until level)
			{
				out += if((lines and (1 shl i2)) > 0) "│ ";
				else "  ";
			}
			out += if(i == initialActions.size - 1) "└─";
			else "├─";

			if(times.size != initialActions.size)
			{
				if(i >= times.size)
					out += "${a.javaClass.simpleName} - ? - %?\n";
				else
					out += "${a.javaClass.simpleName} - ${times[i].toDouble() / 1000} - %?\n";
			}
			else
				out += "${a.javaClass.simpleName} - ${times[i].toDouble() / 1000} - %${
					(times[i].toDouble() / elapsedTime.toDouble() * 100).format(
						1
					)
				}\n";

			if(a is TimerSequentialAction)
			{
				var lines2 = lines;
				if(i != initialActions.size - 1) lines2 = lines2 or (1 shl level);
				out += a.timerString(level + 1, lines2);
			}
			if(a is TimerParallelAction)
			{
				var lines2 = lines;
				if(i != initialActions.size - 1) lines2 = lines2 or (1 shl level);
				out += a.profileString(level + 1, lines2);
			}
		}
		return out;
	}

	override tailrec fun run(p: TelemetryPacket): Boolean
	{
		if(startTime == 0L) startTime = System.currentTimeMillis();
		if(startTime2 == 0L) startTime2 = System.currentTimeMillis();
		if(actions.isEmpty())
		{
			elapsedTime = System.currentTimeMillis() - startTime2;
			return false
		}

		return if(actions.first()
				.run(p)
		)
		{
			true
		}
		else
		{
			times.add(System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();
			actions = actions.drop(1)
			run(p)
		}
	}

	override fun preview(fieldOverlay: Canvas)
	{
		for(a in initialActions)
		{
			a.preview(fieldOverlay)
		}
	}
}

data class TimerParallelAction(
	val initialActions: List<Action>
): Action
{
	private var actions = initialActions

	private val times = Array(initialActions.size) {_ -> 0L};

	private var startTime = 0L;
	private var endTime = 0L;

	constructor(vararg actions: Action): this(actions.asList())

	fun profileString(level: Int = 0, lines: Int = 0): String
	{
		var out = "";
		if(times.size != initialActions.size && level == 0)
			out += "Timing is not complete, some information is not available\n";
		if(level == 0) out += "${javaClass.simpleName} - ${endTime.toDouble() / 1000}\n";

		for((i, a) in initialActions.withIndex())
		{
			for(i2 in 0 until level)
			{
				out += if((lines and (1 shl i2)) > 0) "│ ";
				else "  ";
			}
			out += if(i == initialActions.size - 1) "└─";
			else "├─";

			if(i >= times.size)
				out += "${a.javaClass.simpleName} - ?\n";
			else
				out += "${a.javaClass.simpleName} - ${times[i].toDouble() / 1000}\n";

			if(a is TimerSequentialAction)
			{
				var lines2 = lines;
				if(i != initialActions.size - 1) lines2 = lines2 or (1 shl level);
				out += a.timerString(level + 1, lines2);
			}

			if(a is TimerParallelAction)
			{
				var lines2 = lines;
				if(i != initialActions.size - 1) lines2 = lines2 or (1 shl level);
				out += a.profileString(level + 1, lines2);
			}
		}
		return out;
	}

	override fun run(p: TelemetryPacket): Boolean
	{
		if(startTime == 0L) startTime = System.currentTimeMillis();
		for((i, a) in actions.withIndex())
		{
			if(times[i] == 0L)
			{
				if(!a.run(p))
				{
					times[i] = System.currentTimeMillis() - startTime;
				}
			}
		}
		val run = times.count {elem -> elem == 0L} > 0;
		if(!run)
			endTime = System.currentTimeMillis() - startTime;
		return run;
	}

	override fun preview(fieldOverlay: Canvas)
	{
		for(a in initialActions)
		{
			a.preview(fieldOverlay)
		}
	}
}

fun toTimerAction(action: SequentialAction): TimerSequentialAction
{
	val actions = ArrayList<Action>();
	for(a in action.initialActions)
	{
		when(a)
		{
			is SequentialAction -> actions.add(toTimerAction(a))
			is ParallelAction   -> actions.add(toTimerAction(a))
			else                -> actions.add(a)
		};
	}
	return TimerSequentialAction(actions);
}

fun toTimerAction(action: ParallelAction): TimerParallelAction
{
	val actions = ArrayList<Action>();
	for(a in action.initialActions)
	{
		when(a)
		{
			is SequentialAction -> actions.add(toTimerAction(a))
			is ParallelAction   -> actions.add(toTimerAction(a))
			else                -> actions.add(a)
		};
	}
	return TimerParallelAction(actions);
}