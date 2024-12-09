package org.firstinspires.ftc.teamcode.opmode.auto.Actions

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence

class TrajectoryAction(private val drive: SampleMecanumDrive, private val trajectory: TrajectorySequence) : Action
{
	override fun start()
	{
		drive.followTrajectorySequence(trajectory);
	}

	override fun update(): Boolean
	{
		drive.update();
		return !drive.isBusy;
	}
}