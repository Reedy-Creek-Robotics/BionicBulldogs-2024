package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class HSlide(private val hSlide: Servo)
{
	companion object
	{
		@JvmField
		var increment = 0.05;

		@JvmField
		var max = 0.32;

		@JvmField
		var min = 0.95;

		@JvmField
		var score = 0.32;
	}

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
		hSlide.position += increment;
	}

	fun decrement()
	{
		hSlide.position -= increment;
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
