package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Config
class Slides(private val slides: DcMotor)
{
	companion object
	{
		@JvmField
		var slideSpeed = 0.5;
	}

	init
	{
		slides.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        slides.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
	}

	fun reverse()
	{
		slides.direction = DcMotorSimple.Direction.REVERSE;
	}

	fun up()
	{
		slides.power = slideSpeed;
	}

	fun down()
	{
		slides.power = -slideSpeed;
	}

	fun stop()
	{
		slides.power = 0.0;
	}
}