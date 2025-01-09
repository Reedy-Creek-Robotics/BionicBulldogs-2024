package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

fun rotation(angle: Int): Double
{
	return Math.toRadians(-(angle.toDouble() - 90));
}

fun invRotation(angle: Double): Int
{
	return (-Math.toDegrees(angle) + 90).toInt();
}

fun pos(x: Double, y: Double, angle: Int): Pose2d
{
	return Pose2d(x, y, rotation(angle));
}

fun delay(time: Double, drive: SampleMecanumDrive? = null)
{
	val elapsedTime = ElapsedTime();
	elapsedTime.reset();
	while(elapsedTime.seconds() < time)
		drive?.localizer?.update();
}
