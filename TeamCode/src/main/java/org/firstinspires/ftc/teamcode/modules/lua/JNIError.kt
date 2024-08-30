package org.firstinspires.ftc.teamcode.modules.lua

import java.lang.RuntimeException

class JNIError : RuntimeException
{
	private var msg = "";
	constructor(_msg: String)
	{
		msg = _msg;
	}
	
	override fun getLocalizedMessage(): String
	{
		return msg;
	}
}