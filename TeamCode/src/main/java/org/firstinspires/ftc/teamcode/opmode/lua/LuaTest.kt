package org.firstinspires.ftc.teamcode.opmode.lua

import android.util.Log
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.Ui.UI
import org.firstinspires.ftc.teamcode.modules.lua.Lua

@Autonomous
class LuaTest : LinearOpMode()
{
	private var opmodes: Array<String>? = null;
	private var ui: UI = UI();
	private var selected: String = "";
	var lua: Lua? = null;
	
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		lua = Lua(this);
		opmodes = lua?.init();
		ui.init(telemetry, gamepad1)
		Log.d("LuaTest", opmodes?.size.toString());
		if(opmodes == null)
		{
			return;
		}
		while(opModeInInit())
		{
			if(selected == "")
			{
				ui.label("select opmode");
				for(s in opmodes!!)
				{
					if(ui.button(s))
					{
						selected = s;
						lua?.initRR(selected);
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
		if(lua?.isRR() == true)
		{
			lua?.startRR(selected, 4);
		}
		else
		{
			lua?.start(selected, 4);
		}
		lua?.stop();
	}
}