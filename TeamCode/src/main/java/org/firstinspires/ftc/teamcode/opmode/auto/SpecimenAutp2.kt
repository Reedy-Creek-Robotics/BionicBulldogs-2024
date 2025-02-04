package org.firstinspires.ftc.teamcode.opmode.auto

import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions

@Autonomous
class SpecimenAutp2 : LinearOpMode()
{
	override fun runOpMode()
	{
		val opmodeLoader = OpmodeLoader();
		opmodeLoader.init();
		opmodeLoader.loadOpmode("specimenAuto");

		val builder = opmodeLoader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);

		telemetry.addLine("initalised");
		telemetry.update();

		waitForStart();

		telemetry.clearAll();
		telemetry.update();

		opmodeLoader.start();
	}
}