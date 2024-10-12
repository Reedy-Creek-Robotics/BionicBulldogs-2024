package org.firstinspires.ftc.teamcode.opmode.lua

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.opmodeloader.OpmodeLoader

@Autonomous
class LuaTest2 : LinearOpMode()
{
	override fun runOpMode()
	{
		val e = OpmodeLoader(this);
		
		e.init();
		
		e.loadOpmode("opmode");
		
		waitForStart();
		
		e.startLoop();
	}
}