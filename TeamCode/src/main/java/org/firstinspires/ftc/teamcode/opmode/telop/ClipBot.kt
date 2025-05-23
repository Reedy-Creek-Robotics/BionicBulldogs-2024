package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx

@TeleOp
@Config
class ClipBot: LinearOpMode()
{
	companion object
	{
		@JvmField
		var clipperStart = 0.1;

		@JvmField
		var clipperEnd = 0.36;

		@JvmField
		var clipper3 = 0.27;

		@JvmField
		var clawStart = 0.6;

		@JvmField
		var clawEnd = 0.97;

		@JvmField
		var horizStart = 0.0;

		@JvmField
		var horizEnd = 0.28;
	}

	override fun runOpMode()
	{
		val clipper = hardwareMap.servo.get("clipper"); // 0, 0.35
		val claw = hardwareMap.servo.get("claw"); // 0.6, 0.98
		val horizontal = hardwareMap.servo.get("horizontal"); // 0, 0.28

		var clipperState = 0;
		var clawState = 0;
		var horizontalState = 0;

		waitForStart();

		clipper.position = clipperStart;
		claw.position = clawStart;
		horizontal.position = horizStart;

		val gamepad = GamepadEx(gamepad1);

		while(opModeIsActive())
		{
			if(gamepad.cross())
			{
				if(clipperState == 0)
				{
					clipper.position = clipperEnd;
					clipperState = 1;
				}
				else
				{
					clipper.position = clipperStart;
					clipperState = 0;
				}
			}
			if(gamepad.circle())
			{
				if(clawState == 0)
				{
					claw.position = clawEnd;
					clawState = 1;
				}
				else
				{
					claw.position = clawStart;
					clawState = 0;
				}
			}
			if(gamepad.square())
			{
				if(horizontalState == 0)
				{
					horizontal.position = horizEnd;
					horizontalState = 1;
				}
				else
				{
					horizontal.position = horizStart;
					horizontalState = 0;
				}
			}
			if(gamepad.triangle())
			{
				clipper.position = clipper3;
				clipperState = 1;
			}
			gamepad.copy();
			telemetry.addData("clipper", clipperState);
			telemetry.addData("claw", clawState);
			telemetry.addData("horizontal", horizontalState);
			telemetry.update();
		}
	}
}
