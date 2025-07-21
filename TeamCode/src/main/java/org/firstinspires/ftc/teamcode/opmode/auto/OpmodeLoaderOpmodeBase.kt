package org.firstinspires.ftc.teamcode.opmode.auto

import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.actions.sampleClaw
import org.firstinspires.ftc.teamcode.modules.actions.specimenClaw
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.PI

abstract class OpmodeLoaderOpmodeBase(
	private val name: String,
	private val specOpen: Boolean = false
): LinearOpMode()
{
	override fun runOpMode()
	{
		telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
		MecanumDrive.PARAMS.maxAngVel = PI * 4.0;
		MecanumDrive.PARAMS.maxAngAccel = PI * 4.0;

		initComponents(hardwareMap, telemetry);
		if(specOpen) specimenClaw.open();
		else specimenClaw.close();
		sampleClaw.armGrab();

		val opmodeLoader = OpmodeLoader();

		val builder = opmodeLoader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);

		try
		{
			opmodeLoader.init();

			opmodeLoader.loadOpmode(name);

			telemetry.addLine("initalised");
			telemetry.update();

			waitForStart();
			sampleClaw.armStart();

			telemetry.clearAll();
			telemetry.update();

			opmodeLoader.start();

			opmodeLoader.close();
		}
		catch(e: RuntimeException)
		{
			opmodeLoader.close();
			throw e
		}
	}
}