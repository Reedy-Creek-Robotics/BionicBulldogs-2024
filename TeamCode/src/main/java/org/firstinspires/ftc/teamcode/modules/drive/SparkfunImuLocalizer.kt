package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

var rotPos = 0.0;

class SparkfunImuLocalizer():
	Localizer
{
	private lateinit var imu: SparkFunOTOS;

	override var poseEstimate: Pose2d
		get()
		{
			return Pose2d(0.0, 0.0, imu.position.h);
		}
		set(value)
		{
			imu.position = SparkFunOTOS.Pose2D(0.0, 0.0, value.heading.toDouble());
		}

	constructor(imu2: SparkFunOTOS): this()
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
	}
}
