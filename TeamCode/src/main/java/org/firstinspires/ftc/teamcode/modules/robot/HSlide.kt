package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class HSlide(hardwareMap: HardwareMap)
{
	companion object
	{
		@JvmField
		var increment = 0.1;

		@JvmField
		var max = 0.05;

		@JvmField
		var min = 0.75;

		@JvmField
		var score = 0.05;
	}

	private val hSlide = hardwareMap.servo.get("hslide");

	fun score()
	{
		hSlide.position = score;
	}

	fun pos(): Double
	{
		return hSlide.position;
	}

	fun max(): Double
	{
		return max;
	}

	fun min(): Double
	{
		return min;
	}

	fun increment()
	{
		if(hSlide.position + increment <= min)
			hSlide.position += increment;
		else
			hSlide.position = min;
	}

	fun decrement()
	{
		if(hSlide.position - increment >= max)
			hSlide.position -= increment;
		else
			hSlide.position = max;
	}

	fun zero()
	{
		hSlide.position = max;
	}

	fun gotoPos(pos: Double)
	{
		hSlide.position = pos;
	}
}
