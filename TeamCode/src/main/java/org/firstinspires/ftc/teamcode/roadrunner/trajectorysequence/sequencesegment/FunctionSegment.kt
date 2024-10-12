package org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.sequencesegment

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker

class FunctionSegment : SequenceSegment
{
	constructor(pose: Pose2d?, markers: List<TrajectoryMarker?>?): super(9999999999999.0, pose, pose, markers)
	{
	}

	fun end()
	{
		duration = 0.0;
	}
}