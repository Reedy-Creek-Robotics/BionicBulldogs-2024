package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

var rotPos = 0.0;

class SparkfunImuLocalizer(override var poseEstimate: Pose2d, override val poseVelocity: Pose2d?):
	Localizer
{
	private lateinit var imu: SparkFunOTOS;


	constructor(imu2: SparkFunOTOS): this(Pose2d(), Pose2d())
	{
		imu = imu2;
		imu.angularUnit = AngleUnit.RADIANS;
		imu.angularScalar = 1.0;
		imu.calibrateImu();
		imu.resetTracking();
		imu.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0);
	}

	override fun update()
	{
		poseEstimate = Pose2d(0.0, 0.0, imu.position.h - rotPos);
	}
}
