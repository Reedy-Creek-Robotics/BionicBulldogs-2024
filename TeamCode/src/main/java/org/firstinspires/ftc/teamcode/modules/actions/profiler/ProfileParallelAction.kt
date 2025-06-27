package org.firstinspires.ftc.teamcode.modules.actions.profiler

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

data class ProfileParallelAction(
	val initialActions: List<Action>
): Action
{
	private var actions = initialActions;

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
			out += generateIndent(lines, level, false, true);

			out += if(i >= times.size)
				"${a.javaClass.simpleName} - ?\n";
			else
				"${a.javaClass.simpleName} - ${times[i].toDouble() / 1000}\n";

			if(a is ProfileSequentialAction)
			{
				var lines2 = lines;
				if(i != initialActions.size - 1) lines2 = lines2 or (1 shl level);
				out += a.timerString(level + 1, lines2);
			}

			if(a is ProfileParallelAction)
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
