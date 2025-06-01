package org.firstinspires.ftc.teamcode.opmode

import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaUtils
import org.firstinspires.ftc.teamcode.modules.ui.UI

@Autonomous
class OpmodeLoaderOpmode: LinearOpMode()
{
	override fun runOpMode()
	{
		val ui = UI(telemetry, gamepad1);

		val opmodeLoader = OpmodeLoader();
		var running = true;
		val opmodes = opmodeLoader.init()?:return;

		val builder = opmodeLoader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);
		LuaUtils.init(builder, this);

		while(running && opModeInInit())
		{
			for(opmode in opmodes)
			{
				if(ui.button(opmode))
				{
					running = false;
					opmodeLoader.loadOpmode(opmode);
					break;
				}
			}
			ui.update();
		}
		if(isStopRequested)
			return;
		telemetry.addLine("initalised");
		telemetry.update();

		waitForStart();

		telemetry.clearAll();
		telemetry.update();

		opmodeLoader.start();
	}
}