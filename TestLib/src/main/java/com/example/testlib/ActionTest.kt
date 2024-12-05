package com.example.testlib

import com.example.testlib.actions.*

class ActionTest
{
	fun run()
	{
		val listA = ActionList();

		listA.add(LogAction("a"));
		listA.add(DelayAction(1.0));
		listA.add(LogAction("a"));
		listA.add(DelayAction(5.0));
		listA.add(SyncAction());
		listA.add(LogAction("a"));


		val listB = ActionList();

		listB.add(LogAction("b"));
		listB.add(DelayAction(2.0));
		listB.add(LogAction("b"));
		listB.add(DelayAction(2.0));
		listB.add(SyncAction());
		listB.add(LogAction("b"));

		val manager = ActionManager(listA, listB);

		manager.run();
	}
}