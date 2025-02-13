package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.roadrunner.Pose2d
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

import org.firstinspires.ftc.teamcode.modules.hardware.ImuEx

class IMULocalizer(): Localizer
{
	private var offset = 0.0;
	override var poseEstimate: Pose2d
		get()
		{
			return Pose2d(0.0, 0.0, imu.getHeading(AngleUnit.RADIANS).toDouble() + offset)
		}
		set(value)
		{
			offset = value.heading.toDouble();
			imu.imu.resetYaw();
		}

	private lateinit var imu: ImuEx;

	constructor(imu2: ImuEx): this()
	{
		imu = imu2;
	}

	override fun update()
	{
	}
}