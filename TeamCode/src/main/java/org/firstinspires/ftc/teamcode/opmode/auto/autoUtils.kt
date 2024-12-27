package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.util.ElapsedTime

fun rotation(angle: Int): Double
{
	return Math.toRadians(-(angle.toDouble() - 90))
}

fun pos(x: Double, y: Double, angle: Int): Pose2d
{
	return Pose2d(x, y, rotation(angle));
}

fun delay(time: Double)
{
  val elapsedTime = ElapsedTime();
  elapsedTime.reset();
  while(elapsedTime.seconds() < time);
}
