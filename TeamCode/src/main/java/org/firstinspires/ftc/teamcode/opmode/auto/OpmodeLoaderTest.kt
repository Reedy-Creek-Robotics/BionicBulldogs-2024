package org.firstinspires.ftc.teamcode.opmode.auto

import com.minerkid08.dynamicopmodeloader.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.actions.initComponents
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaAction
import org.firstinspires.ftc.teamcode.modules.opmodeloader.LuaRobotActions

@Autonomous
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