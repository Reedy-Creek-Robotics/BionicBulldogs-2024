package org.firstinspires.ftc.teamcode.modules.lua

import java.lang.RuntimeException

class PathError : RuntimeException
{
	var msg: String = "";
	constructor(m: String)
	{
		msg = m;
	}
	
	override fun getLocalizedMessage(): String
	{
		return msg;
	}
}