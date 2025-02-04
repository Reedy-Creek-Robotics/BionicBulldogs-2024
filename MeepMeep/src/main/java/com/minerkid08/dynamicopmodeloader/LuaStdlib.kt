package com.minerkid08.dynamicopmodeloader

class LuaStdlib
{
	fun print(string: String)
	{
		kotlin.io.print(string);
	}
	
	fun err(msg: String)
	{
		throw LuaError(msg);
	}
}