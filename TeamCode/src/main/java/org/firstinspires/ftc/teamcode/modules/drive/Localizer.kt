package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.roadrunner.Pose2d

interface Localizer
{
	var poseEstimate: Pose2d
	fun update();
}