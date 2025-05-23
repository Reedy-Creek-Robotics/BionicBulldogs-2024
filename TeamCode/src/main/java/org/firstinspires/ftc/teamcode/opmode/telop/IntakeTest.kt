package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx

@TeleOp
@Config
class IntakeTest: LinearOpMode()
{
	companion object
	{
		@JvmField
		var s1Power = -1.0;

		@JvmField
		var s2Power = -1.0;
	}

	override fun runOpMode()
	{
		val s1 = hardwareMap.crservo.get("servo");
		val s2 = hardwareMap.crservo.get("servo2");
		val gamepad = GamepadEx(gamepad1);

		var state = 0;

		waitForStart();

		while(opModeIsActive())
		{
			gamepad.copy();
			if(gamepad.cross())
			{
				when(state)
				{
					0, 2 ->
					{
						s1.power = s1Power;
						s2.power = s2Power;
						state = 1;
					}

					1    ->
					{
						s1.power = 0.0;
						s2.power = 0.0;
						state = 0;
					}
				}
			}
			if(gamepad.circle())
			{
				when(state)
				{
					0, 1 ->
					{
						s1.power = -s1Power;
						s2.power = -s2Power;
						state = 2;
					}

					2    ->
					{

						s1.power = 0.0;
						s1.power = 0.0;
						state = 0;
					}
				}
			}
		}
	}
}