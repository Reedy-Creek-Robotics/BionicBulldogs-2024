package org.firstinspires.ftc.teamcode.modules.actions.profiler

fun generateIndent(lines: Int, level: Int, end: Boolean, fork: Boolean): String
{
	var out = "";
	for(i2 in 0 until level)
	{
		out += if((lines and (1 shl i2)) > 0) "│ ";
		else "  ";
	}
	out += if(fork)
	{
		if(end) "└─";
		else "├─";
	}
	else
	{
		if(end) "  ";
		else "│ ";
	}
	return out;
}