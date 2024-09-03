package org.firstinspires.ftc.teamcode.modules

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class TestModule(hardwareMap: HardwareMap)
{
	val servo: Servo = hardwareMap.servo.get("servo");
	val servo2: Servo = hardwareMap.servo.get("servo2");
	
	fun setPos(pos: Double)
	{
		servo.position = pos;
	}
	
	fun setPos2(pos: Double)
	{
		servo2.position = pos;
	}
}