package org.firstinspires.ftc.teamcode.opmode.auto

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class LimeLight2 : LinearOpMode()
{
	override fun runOpMode()
	{
		val limeLight = hardwareMap.get(Limelight3A::class.java, "limelight");
		limeLight.setPollRateHz(100);
		limeLight.start();
		limeLight.pipelineSwitch(0);
		waitForStart();

		while(opModeIsActive())
		{
			val results = limeLight.latestResult.colorResults;
			telemetry.addLine("num results: ${results.size}");
			telemetry.update();
		}
	}
}