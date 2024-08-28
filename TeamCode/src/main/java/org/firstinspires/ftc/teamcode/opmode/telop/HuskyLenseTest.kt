package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.dfrobot.HuskyLens
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class HuskyLenseTest : LinearOpMode()
{
	override fun runOpMode()
	{
		telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry);
		val huskyLens = hardwareMap.get(HuskyLens::class.java, "huskyLens");
		huskyLens.initialize();
		if(!huskyLens.knock())
			return;
		huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
		waitForStart();
		while(opModeIsActive())
		{
			val blocks = huskyLens.blocks();
			for(block in blocks)
			{
				telemetry.addData("${block.id}x", block.x);
				telemetry.addData("${block.id}y", block.y);
				telemetry.addData("${block.id}h", block.height);
				telemetry.addData("${block.id}w", block.width);
			}
			telemetry.update();
		}
	}
}