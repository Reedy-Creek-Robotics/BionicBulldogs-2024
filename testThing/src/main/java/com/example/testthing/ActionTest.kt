package org.firstinspires.ftc.teamcode.opmode.auto

import org.firstinspires.ftc.teamcode.opmode.auto.Actions.ActionList
import org.firstinspires.ftc.teamcode.opmode.auto.Actions.EndAction
import org.firstinspires.ftc.teamcode.opmode.auto.Actions.FunctionAction
import org.firstinspires.ftc.teamcode.opmode.auto.Actions.SyncAction

class ActionTest
{
	private fun getActionListA(): ActionList
	{
		val actions = ActionList();

		actions.add(FunctionAction({println("a");}));
		actions.add(DelayAction(1.0));
		actions.add(FunctionAction({println("a");}));
		actions.add(DelayAction(5.0));
		actions.add(SyncAction());
		actions.add(FunctionAction({println("a");}));

		return actions;
	}

	private fun getActionListB(): ActionList
	{
		val actions = ActionList();

		actions.add(FunctionAction({println("b")}));
		actions.add(DelayAction(2.0));
		actions.add(FunctionAction({println("b")}));
		actions.add(DelayAction(2.0));
		actions.add(SyncAction());
		actions.add(FunctionAction({println("b")}));

		return actions;
	}

	fun runOpMode()
	{
		val actionsA = getActionListA();
		val actionsB = getActionListA();

		var actionA = actionsA.next();
		var actionB = actionsA.next();

		actionA.start();
		actionB.start();

		while(true)
		{
			if(actionA is EndAction && actionB is EndAction) return;
			if(actionA is SyncAction && actionB is SyncAction)
			{
				actionA = actionsA.next();
				actionB = actionsB.next();
				actionA.start();
				actionB.start();
				continue;
			}

			if(actionA.update())
			{
				actionA = actionsA.next();
				actionA.start();
			}
			if(actionB.update())
			{
				actionB = actionsB.next();
				actionB.start();
			}
		}
	}
}