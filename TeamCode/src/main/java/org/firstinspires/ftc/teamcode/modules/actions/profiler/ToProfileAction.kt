package org.firstinspires.ftc.teamcode.modules.actions.profiler

import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction

fun toProfileAction(action: SequentialAction): ProfileSequentialAction
{
	val actions = ArrayList<Action>();
	for(a in action.initialActions)
	{
		when(a)
		{
			is SequentialAction -> actions.add(toProfileAction(a))
			is MarkerSequentialAction -> actions.add(toProfileAction(a))
			is ParallelAction -> actions.add(toProfileAction(a))
			else -> actions.add(a)
		};
	}
	return ProfileSequentialAction(actions);
}

fun toProfileAction(action: ParallelAction): ProfileParallelAction
{
	val actions = ArrayList<Action>();
	for(a in action.initialActions)
	{
		when(a)
		{
			is SequentialAction -> actions.add(toProfileAction(a))
			is MarkerSequentialAction -> actions.add(toProfileAction(a))
			is ParallelAction -> actions.add(toProfileAction(a))
			else -> actions.add(a)
		};
	}
	return ProfileParallelAction(actions);
}

fun toProfileAction(action: MarkerSequentialAction): ProfileSequentialAction
{
	val actions = ArrayList<Action>();
	for(a in action.initialActions)
	{
		when(a)
		{
			is SequentialAction -> actions.add(toProfileAction(a))
			is MarkerSequentialAction -> actions.add(toProfileAction(a))
			is ParallelAction -> actions.add(toProfileAction(a))
			else -> actions.add(a)
		};
	}
	return ProfileSequentialAction(actions, action.label);
}
