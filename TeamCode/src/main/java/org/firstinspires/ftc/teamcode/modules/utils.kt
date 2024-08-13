package org.firstinspires.ftc.teamcode.modules

fun clamp(v: Float, min: Float, max: Float): Float
{
	if(v > max)
		return max;
	if(v < min)
		return min;
	return v;
}