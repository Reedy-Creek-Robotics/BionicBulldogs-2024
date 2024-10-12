package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

fun velOverride(maxVel: Double) : TrajectoryVelocityConstraint
{
	return SampleMecanumDrive.getVelocityConstraint(maxVel, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH);
}

fun accelOverride() : TrajectoryAccelerationConstraint
{
	return SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL);
}