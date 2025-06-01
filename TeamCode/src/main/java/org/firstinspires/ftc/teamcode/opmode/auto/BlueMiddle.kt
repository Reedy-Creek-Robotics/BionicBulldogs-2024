package org.firstinspires.ftc.teamcode.opmode.auto

import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.actions.specimenClaw
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions
import org.firstinspires.ftc.teamcode.opmode.telop.OuttakeTelop
import org.firstinspires.ftc.teamcode.opmode.telop.OuttakeTelop.Companion.armUp2
import org.firstinspires.ftc.teamcode.opmode.telop.OuttakeTelop.Companion.clawOpen
import org.firstinspires.ftc.teamcode.opmode.telop.OuttakeTelop.Companion.intakeRotatorPos
import org.firstinspires.ftc.teamcode.opmode.telop.OuttakeTelop.Companion.rotatorDown
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.PI

@Autonomous
class BlueMiddle : LinearOpMode()
{
	override fun runOpMode()
	{
		val claw = hardwareMap.servo.get("outtakeClaw");
		val outtakeArm = hardwareMap.servo.get("outtakeArm");
		val clawRotator = hardwareMap.servo.get("clawRotator");
		claw.position = clawOpen;
		outtakeArm.position = armUp2;
		clawRotator.position = rotatorDown;

		MecanumDrive.PARAMS.maxAngVel = PI * 4.0;
		MecanumDrive.PARAMS.maxAngAccel = PI * 4.0;

		initComponents(hardwareMap);
		specimenClaw.open();

		val opmodeLoader = OpmodeLoader();

		opmodeLoader.init();

		val builder = opmodeLoader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);

		opmodeLoader.loadOpmode("blueMiddle");

		telemetry.addLine("initalised");
		telemetry.update();

		waitForStart();

		telemetry.clearAll();
		telemetry.update();

		opmodeLoader.start();
	}
}