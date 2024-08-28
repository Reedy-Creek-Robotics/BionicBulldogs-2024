package org.firstinspires.ftc.teamcode.opmode.lua

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.lua.Lua
import org.firstinspires.ftc.teamcode.modules.ui.UI

@Autonomous
class LuaTest : LinearOpMode()
{
	private lateinit var opmodes: Array<String>;
	private val ui: UI = UI();
	private var selected: String = "";
	
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		val lua = Lua(this);

		opmodes = lua.init();
		
		ui.init(telemetry, gamepad1)

		while(opModeInInit())
		{
			if(selected == "")
			{
				ui.label("select opmode");
				for(s in opmodes)
				{
					if(ui.button(s))
					{
						selected = s;
					}
				}
			}
			else
			{
				ui.label("$selected selected");
			}
			ui.update();
		}
		if(!opModeIsActive())
			return;
		lua.start(selected);
		val elapsedTime = ElapsedTime();
		elapsedTime.reset();
		var prevTime = 0.0f;
		while(opModeIsActive())
		{
			val curTime = elapsedTime.seconds().toFloat();
			val deltaTime: Float = curTime - prevTime;
			lua.update(curTime, deltaTime);
			prevTime = curTime;
		}
	}
}