package com.example.testlib.actions

class ActionManager(private val listA: ActionList, private val listB: ActionList)
{
	private var actionA = listA.next();
	private var actionB = listB.next();

	fun runAsync()
	{
		actionA.start();
		actionB.start();
	}

	fun run()
	{
		runAsync();
		while(!update());
	}

	fun update(): Boolean
	{
		if(actionA is EndAction && actionB is EndAction) return true;
		if(actionA is SyncAction && actionB is SyncAction)
		{
			actionA = listA.next();
			actionB = listB.next();
			actionA.start();
			actionB.start();
			return false;
		}

		if(actionA.update())
		{
			actionA = listA.next();
			actionA.start();
		}
		if(actionB.update())
		{
			actionB = listB.next();
			actionB.start();
		}
		return false;
	}
}