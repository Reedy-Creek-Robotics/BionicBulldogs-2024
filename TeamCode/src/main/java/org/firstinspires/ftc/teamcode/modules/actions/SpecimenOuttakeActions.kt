package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

class SpecimenOuttakeAction_Grab: Action
{
	private var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			specimenOuttake.collect();
			ran = true;
		}
		specimenOuttake.update();
		return specimenOuttake.isBusy();
	}
}

class SpecimenOuttakeAction_Score: Action
{
	private var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			specimenOuttake.score();
			ran = true;
		}
		specimenOuttake.update();
		return specimenOuttake.isBusy();
	}
}