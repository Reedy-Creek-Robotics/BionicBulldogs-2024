package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action

class SampleOuttakeAction_Grab: Action
{
	private var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			sampleOuttake.up();
			ran = true;
		}
		sampleOuttake.update();
		return sampleOuttake.isBusy();
	}
}

class SampleOuttakeAction_Score: Action
{
	private var ran = false;
	override fun run(p: TelemetryPacket): Boolean
	{
		if(!ran)
		{
			sampleOuttake.score();
			ran = true;
		}
		sampleOuttake.update();
		return sampleOuttake.isBusy();
	}
}

class SampleOuttakeAction_Park: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		outtake.park();
		return false;
	}
}