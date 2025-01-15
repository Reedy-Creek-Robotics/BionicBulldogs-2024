package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.AccelConstraint
import com.acmerobotics.roadrunner.ProfileAccelConstraint
import com.acmerobotics.roadrunner.TranslationalVelConstraint
import com.acmerobotics.roadrunner.VelConstraint
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

fun velOverrideRaw(maxVelocity: Double): VelConstraint
{
	return TranslationalVelConstraint(maxVelocity);
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