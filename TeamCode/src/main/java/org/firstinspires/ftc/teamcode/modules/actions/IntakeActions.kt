package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.ColorSensor

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

class IntakeAction_WaitForColor(private val color: Int): Action
{
	val elapsedTime = ElapsedTime();
	var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			ran = true;
			elapsedTime.reset();
		}
		if(elapsedTime.seconds() > 2)
			return false;
		colorSensor.update();
		if(colorSensor.col == color)
			return false;
		return true;
	}
}