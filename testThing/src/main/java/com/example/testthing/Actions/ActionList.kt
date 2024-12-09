package org.firstinspires.ftc.teamcode.opmode.auto.Actions

class ActionList
{
	val list = ArrayList<Action>();
	var i = 0;

	fun add(action: Action)
	{
		list.add(action);
	}

	fun next(): Action
	{
		if(list.size > i)
			return list[i++];
		return EndAction();
	}
}