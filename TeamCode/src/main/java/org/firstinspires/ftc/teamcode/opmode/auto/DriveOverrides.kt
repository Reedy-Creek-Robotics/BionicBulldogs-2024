package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

fun velOverride(maxVel: Double = -1.0, maxAngularVel: Double = -1.0): TrajectoryVelocityConstraint
{
	val v = if(maxVel > 0) maxVel else DriveConstants.MAX_VEL;
	val a = if(maxAngularVel > 0) maxAngularVel else DriveConstants.MAX_ANG_VEL;
	return SampleMecanumDrive.getVelocityConstraint(v, a, DriveConstants.TRACK_WIDTH);
}

fun accelOverride(maxAccel: Double = -1.0): TrajectoryAccelerationConstraint
{
	val v = if(maxAccel > 0) maxAccel else DriveConstants.MAX_ACCEL;
	return SampleMecanumDrive.getAccelerationConstraint(v);
}