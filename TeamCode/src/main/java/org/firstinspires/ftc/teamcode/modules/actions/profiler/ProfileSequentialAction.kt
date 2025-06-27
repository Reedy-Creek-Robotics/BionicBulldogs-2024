package org.firstinspires.ftc.teamcode.modules.actions.profiler

import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.modules.format

data class ProfileSequentialAction(
	val initialActions: List<Action>,
	val label: String? = null
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
			if(a is MarkerAction || (a is ProfileSequentialAction && a.label != null))
			{
				out += generateIndent(lines, level, false, false);
				out += '\n';
			}

			out += generateIndent(lines, level, i == initialActions.size - 1, true);

			out += if(a is MarkerAction)
				a.getLabel();
			else if(a is ProfileSequentialAction && a.label != null)
				"${a.javaClass.simpleName}: ${a.label}";
			else
				a.javaClass.simpleName;

			out += if(times.size != initialActions.size)
			{
				if(i >= times.size)
					" - ? - ?%\n";
				else
					" - ${times[i].toDouble() / 1000} - ?%\n";
			}
			else
				" - ${times[i].toDouble() / 1000} - ${
					(times[i].toDouble() / elapsedTime.toDouble() * 100).format(
						1
					)
				}%\n";

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