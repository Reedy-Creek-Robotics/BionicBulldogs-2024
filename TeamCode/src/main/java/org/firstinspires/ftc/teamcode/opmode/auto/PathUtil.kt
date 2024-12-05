package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d

val startX = 10.0;
val startY = -60.0;

val sampleX = arrayOf(38.0, 46.0, 56.0);
val sampleY = -20.0;

val specimineScoreY = -29.0;

val specimenDropoffY = -40.0;
val specimenCollectY = -55.0;

val noPartner = true;

fun rotation(angle: Int): Double
{
	return Math.toRadians(-(angle.toDouble() - 90))
}

fun pos(x: Double, y: Double, angle: Int): Pose2d
{
	return Pose2d(x, y, rotation(angle));
}