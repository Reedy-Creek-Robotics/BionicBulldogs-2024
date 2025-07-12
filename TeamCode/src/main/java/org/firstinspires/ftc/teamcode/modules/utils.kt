package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.robotcore.external.Telemetry

fun clamp(v: Float, min: Float, max: Float): Float
{
		if(v > max) return max;
		if(v < min) return min;
		return v;
}

fun lerp(a: Float, b: Float, t: Float): Float
{
		return a + (b - a) * t;
}

fun Double.format(len: Int) = "%.${len}f".format(this);
fun Float.format(len: Int) = "%.${len}f".format(this);

fun Telemetry.fmt(fmt: String, vararg args: Any)
{
	addLine(fmt.format(args));
}