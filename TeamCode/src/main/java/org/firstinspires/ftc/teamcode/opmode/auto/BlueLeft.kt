package org.firstinspires.ftc.teamcode.opmode.auto

import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.actions.sampleClaw
import org.firstinspires.ftc.teamcode.modules.actions.specimenClaw
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.PI

@Autonomous
class BlueLeft: LinearOpMode()
{
	override fun runOpMode()
	{
		MecanumDrive.PARAMS.maxAngVel = PI * 4.0;
		MecanumDrive.PARAMS.maxAngAccel = PI * 4.0;

		initComponents(hardwareMap, telemetry);
		specimenClaw.close();
		sampleClaw.armGrab();

		val opmodeLoader = OpmodeLoader();

		val builder = opmodeLoader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);

		opmodeLoader.init();

		opmodeLoader.loadOpmode("blueLeft");

		telemetry.addLine("initalised");
		telemetry.update();

		waitForStart();
		sampleClaw.armStart();

		telemetry.clearAll();
		telemetry.update();

		opmodeLoader.start();
	}
}
