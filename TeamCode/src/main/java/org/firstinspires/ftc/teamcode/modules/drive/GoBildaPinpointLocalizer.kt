package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.modules.hardware.GoBildaPinpoint

class GoBildaPinpointLocalizer(hardwareMap: HardwareMap): Localizer
{
	private var imu: GoBildaPinpoint = hardwareMap.get(GoBildaPinpoint::class.java, "pinpoint");

	override var poseEstimate: Pose2d
		get() = Pose2d(imu.posX, imu.posY, imu.heading);
		set(value)
		{
			imu.setPosition(
				Pose2D(
					DistanceUnit.INCH,
					0.0,
					0.0,
					AngleUnit.RADIANS,
					value.heading.toDouble()
				)
			);
		}

	init
	{
		imu.initialize();
	}

	override fun update()
	{
		imu.update();
	}
}