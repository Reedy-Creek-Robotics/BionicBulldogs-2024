package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.*
import org.firstinspires.ftc.teamcode.modules.actions.drive
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

fun velOverrideRaw(maxVelocity: Double = 0.0, maxAngVel: Double = 0.0): VelConstraint
{
	return MinVelConstraint(
		listOf(
			drive.kinematics.WheelVelConstraint(if(maxVelocity != 0.0) maxVelocity else MecanumDrive.PARAMS.maxWheelVel),
			AngularVelConstraint(if(maxAngVel != 0.0) maxAngVel else MecanumDrive.PARAMS.maxAngVel)
		)
	);
}

fun accelOverrideRaw(
	minAccel: Double = Double.POSITIVE_INFINITY,
	maxAccel: Double = Double.POSITIVE_INFINITY
): AccelConstraint
{
	var minAccel2 = minAccel;
	if(minAccel2 == Double.POSITIVE_INFINITY)
		minAccel2 = MecanumDrive.PARAMS.minProfileAccel;

	var maxAccel2 = maxAccel;
	if(maxAccel2 == Double.POSITIVE_INFINITY)
		maxAccel2 = MecanumDrive.PARAMS.maxProfileAccel;

	return ProfileAccelConstraint(minAccel2, maxAccel2);
}