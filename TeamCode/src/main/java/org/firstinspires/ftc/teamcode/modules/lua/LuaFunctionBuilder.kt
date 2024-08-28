package org.firstinspires.ftc.teamcode.modules.lua

enum class LuaType(val id: Int)
{
	Float(3), Bool(1), String(4), Void(0), CheckRun(-1);
}

class LuaFunctionBuilder
{
	/**
	 * sets the object to pull functions from to expose to lua
	 */
	external fun <T> setCurrentObject(thing: T);
	
	/**
	 * starts a new lua table for functions
	 */
	external fun newClass();
	
	/**
	 * ends the table
	 */
	external fun endClass(name: String);
	
	/**
	 * adds a function from java to lua
	 *
	 * if a class is active when this is called the function will be put in that table
	 * else the function will be set as a global
	 */
	fun addFun(name: String, rtnType: LuaType, argTypes: List<LuaType>)
	{
		var funSignature = "(";
		for(type in argTypes)
		{
			if(type == LuaType.Void || type == LuaType.CheckRun)
				throw LuaError("function argument type cannot be void");
			funSignature += typeToStr(type);
		}
		funSignature += ')';
		funSignature += typeToStr(rtnType);
		addFunction(name, funSignature, rtnType.id, argTypes.size)
	}
	
	private external fun addFunction(name: String, funSignature: String, rtnType: Int, argc: Int);
	
	private fun typeToStr(type: LuaType): String
	{
		if(type == LuaType.Float)
			return "F";
		if(type == LuaType.Bool)
			return "Z";
		if(type == LuaType.Void)
			return "V";
		if(type == LuaType.CheckRun)
			return "Z";
		if(type == LuaType.String)
			return "Ljava/lang/string;";
		return "what";
	}
}

