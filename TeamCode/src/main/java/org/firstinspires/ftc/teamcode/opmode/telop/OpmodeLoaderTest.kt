package org.firstinspires.ftc.teamcode.opmode.telop

import com.minerkid08.dynamicopmodeloader.*
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions

@TeleOp
class OpmodeLoaderTest: LinearOpMode()
{
	override fun runOpMode()
	{
		initComponents(hardwareMap);
		val opmodeloader = OpmodeLoader();
		opmodeloader.init();

		val builder = opmodeloader.getFunctionBuilder();
		LuaAction.init(builder);
		LuaRobotActions.init(builder);

		opmodeloader.loadOpmode("testOpmode");

		waitForStart();

		opmodeloader.start();
	}
}