package org.firstinspires.ftc.teamcode.opmode.auto

import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.PI

@Autonomous
class SpecimenAutp5 : LinearOpMode()
{
	override fun runOpMode()
	{
		MecanumDrive.PARAMS.maxAngVel = PI * 4.0;
		MecanumDrive.PARAMS.maxAngAccel = PI * 4.0;

		initComponents(hardwareMap);
		val opmodeLoader = OpmodeLoader();

		opmodeLoader.init();

		val builder = opmodeLoader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);

		opmodeLoader.loadOpmode("specimenAuto5");

		telemetry.addLine("initalised");
		telemetry.update();

		waitForStart();

		telemetry.clearAll();
		telemetry.update();

		opmodeLoader.start();
	}
}
