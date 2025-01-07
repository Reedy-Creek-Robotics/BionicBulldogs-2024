package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

fun velOverride(maxVel: Double = -1.0, maxAngularVel: Double = -1.0): TrajectoryVelocityConstraint
{
	val vel = DriveConstants.MAX_VEL * (if (maxVel == -1.0) 1.0 else maxVel);
	val angVel = DriveConstants.MAX_ANG_VEL * (if (maxAngularVel == -1.0) 1.0 else maxAngularVel);
	
	return SampleMecanumDrive.getVelocityConstraint(vel, angVel, DriveConstants.TRACK_WIDTH);
}

fun velOverrideRaw(
	maxVel: Double = -1.0,
	maxAngularVel: Double = -1.0
): TrajectoryVelocityConstraint
{
	val vel = if (maxVel == -1.0) DriveConstants.MAX_VEL else maxVel;
	val angVel = if (maxAngularVel == -1.0) DriveConstants.MAX_ANG_VEL else maxAngularVel;
	
	return SampleMecanumDrive.getVelocityConstraint(vel, angVel, DriveConstants.TRACK_WIDTH);
}

fun accelOverride(maxAccel: Double = -1.0): TrajectoryAccelerationConstraint
{
	val accel = DriveConstants.MAX_ACCEL * (if (maxAccel == -1.0) 1.0 else maxAccel);
	
	return SampleMecanumDrive.getAccelerationConstraint(accel);
}

fun accelOverrideRaw(maxAccel: Double = -1.0): TrajectoryAccelerationConstraint
{
	val accel = if (maxAccel == -1.0) DriveConstants.MAX_ACCEL else maxAccel;
	
	return SampleMecanumDrive.getAccelerationConstraint(accel);
}
