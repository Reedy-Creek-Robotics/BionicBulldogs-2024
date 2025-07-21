package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.ColorSensor
import org.firstinspires.ftc.teamcode.modules.robot.Intake

class IntakeAction_Intake: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.forward();
		return false;
	}
}

class IntakeAction_Outtake: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.reverse();
		return false;
	}
}

class IntakeAction_Stop: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.stop();
		return false;
	}
}

class IntakeAction_ZeroRotator(): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.zeroRotator();
		return false;
	}
}

class IntakeAction_SetRotation(private val rot: Double): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		intake.setRotatorPos(rot);
		return false;
	}
}

class IntakeAction_WaitForColor(private val color: Int, private val maxDelay: Double): Action
{
	private val elapsedTime = ElapsedTime();
	private var ran = false;
	private var outtakeTime = 0.0;

	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			ran = true;
			elapsedTime.reset();
			intake.forward();
		}
		colorSensor.update();
		colorSensor.telem(telemetry);

		if(colorSensor.col == color)
			return false;

		when(intake.state)
		{
			Intake.State.Reverse ->
			{
				if(outtakeTime == 0.0 && colorSensor.col == ColorSensor.NONE)
					outtakeTime = elapsedTime.seconds();
				else if(outtakeTime < elapsedTime.seconds() + 0.5)
				{
					intake.forward();
					outtakeTime = 0.0;
				}
			}

			Intake.State.Forward ->
			{
				if(colorSensor.col != ColorSensor.YELLOW && colorSensor.col != ColorSensor.NONE && outtakeTime == 0.0)
				{
					outtakeTime = elapsedTime.seconds();
				}

				if(outtakeTime > 0 && elapsedTime.seconds() - outtakeTime > 0.5)
				{
					intake.reverse();
					outtakeTime = 0.0;
				}
			}

			Intake.State.Stop    ->
			{
			}
		}

		if(elapsedTime.seconds() > maxDelay)
			return false;
		return true;
	}
}