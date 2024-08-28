package org.firstinspires.ftc.teamcode.modules.lua

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Servo

class TestModule(opmode: OpMode)
{
	private var servo: Servo;
	private var servo2: Servo;
	
	init
	{
		servo = opmode.hardwareMap.servo.get("servo");
		servo2 = opmode.hardwareMap.servo.get("servo2");
	}
	
	fun setPos(pos: Float)
	{
		servo.position = pos.toDouble();
	}
	
	fun setPos2(pos: Float)
	{
		servo2.position = pos.toDouble();
	}
}