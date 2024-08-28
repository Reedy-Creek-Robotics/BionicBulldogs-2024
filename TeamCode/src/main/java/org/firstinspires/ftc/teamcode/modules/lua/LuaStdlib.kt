package org.firstinspires.ftc.teamcode.modules.lua

import android.os.Environment
import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

class LuaStdlib(val opmode: LinearOpMode)
{
	fun getDataDir(): String
	{
		return Environment.getExternalStorageDirectory().toString();
	}

	fun print(string: String)
	{
		Log.d("Lua", string);
	}

	fun err(msg: String)
	{
		throw LuaError(msg);
	}

	fun jniErr(msg: String)
	{
		throw JNIError(msg);
	}

	fun telem(label: String, msg: String)
	{
		opmode.telemetry.addData(label, msg);
	}

	fun updateTelem()
	{
		opmode.telemetry.update();
	}

	fun delay(time: Double): Boolean
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time && opmode.opModeIsActive());

		return !opmode.opModeIsActive();
	}

	fun checkRunning(): Boolean
	{
		return !opmode.opModeIsActive();
	}

	fun isActive(): Boolean
	{
		return opmode.opModeIsActive();
	}
}