package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.modules.clamp

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
		var min = 0.65;

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
		val pos2 = clamp(pos, max, min);
		hSlide.position = pos2;
	}
}
