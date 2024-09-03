package org.firstinspires.ftc.teamcode.opmode.lua

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.TestModule
import org.firstinspires.ftc.teamcode.modules.ui.UI
import org.firstinspires.ftc.teamcode.opmodeloader.OpmodeLoaderRR
import org.firstinspires.ftc.teamcode.opmodeloader.LuaType
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

@Autonomous
class LuaRoadrunnerTest : LinearOpMode()
{
	private lateinit var opmodes: Array<String>;
	private val ui: UI = UI();
	private var selected: String = "";
	
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		val drive = SampleMecanumDrive(hardwareMap);
		val luaRR = OpmodeLoaderRR(drive, this);
		
		opmodes = luaRR.init();

		val builder = luaRR.getFunctionBuilder();

		val servos = TestModule(hardwareMap);

		builder.setCurrentObject(servos);

		builder.newClass();
		builder.addFun("setPos", LuaType.Void, listOf(LuaType.Double));
		builder.addFun("setPos2", LuaType.Void, listOf(LuaType.Double));
		builder.endClass("servos");

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
						luaRR.loadOpmode(selected);
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
		luaRR.start();
	}
}