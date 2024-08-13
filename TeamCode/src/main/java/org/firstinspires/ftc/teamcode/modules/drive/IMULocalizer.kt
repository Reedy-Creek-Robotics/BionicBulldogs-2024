package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

import org.firstinspires.ftc.teamcode.modules.hardware.ImuEx

class IMULocalizer(override var poseEstimate: Pose2d, override val poseVelocity: Pose2d?) : Localizer
{
	lateinit var imu: ImuEx;
	constructor(imu2: ImuEx) : this(Pose2d(), Pose2d())
	{
		imu = imu2;
	}
	
	override fun update()
	{
		poseEstimate = Pose2d(0.0, 0.0, imu.getHeading(AngleUnit.RADIANS).toDouble());
	}
}