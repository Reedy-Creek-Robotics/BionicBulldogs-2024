package org.firstinspires.ftc.teamcode.opmode.auto

import com.example.testlib.actions.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class ActionTest : LinearOpMode()
{
	override fun runOpMode()
	{
		val servoA = hardwareMap.servo.get("servo");
		val servoB = hardwareMap.servo.get("servo2");

		servoA.position = 0.5;
		servoB.position = 0.5;

		val listA = ActionList();
		listA.add(FunctionAction(startFun = {servoA.position = 0.0;}));
		listA.add(DelayAction(2.0));
		listA.add(FunctionAction(startFun = {servoA.position = 1.0;}));
		listA.add(SyncAction());
		listA.add(FunctionAction(startFun = {servoA.position = 0.0;}));

		val listB = ActionList();
		listB.add(FunctionAction(startFun = {servoB.position = 0.0;}));
		listB.add(DelayAction(3.0));
		listB.add(FunctionAction(startFun = {servoB.position = 1.0;}));
		listB.add(DelayAction(2.0));
		listB.add(SyncAction());
		listB.add(FunctionAction(startFun = {servoB.position = 0.0;}));

		val actionManager = ActionManager(listA, listB);

		waitForStart();

		actionManager.runAsync();

		while(opModeIsActive())
		{
			if(actionManager.update())
				return;
		}
	}
}