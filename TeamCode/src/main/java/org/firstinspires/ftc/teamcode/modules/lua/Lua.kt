package org.firstinspires.ftc.teamcode.modules.lua

import android.os.Environment
import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class Lua(a: LinearOpMode)
{
	companion object
	{
		init
		{
			System.loadLibrary("ftcrobotcontroller");
		}
	}
	
	val opmode: LinearOpMode = a;
	private var trajectory: ArrayList<TrajectorySequence> = ArrayList<TrajectorySequence>();
	private var lrr: LuaRoadRunner? = null;
	
	external fun init(): Array<String>;
	external fun <T> addObject(thing: T);
	external fun start(name: String, recognition: Int);
	external fun stop();
	external fun update(delatTime: Float, elapsedTime: Float);
	
	fun initRR(name: String)
	{
		lrr = LuaRoadRunner(opmode);
		opmode.telemetry.clearAll();
		opmode.telemetry.addLine("building path 1");
		opmode.telemetry.update();
		lrr?.buildPath(name, 0);
		lrr?.getTrajectory()?.let { trajectory.add(it) };
		opmode.telemetry.clearAll();
		opmode.telemetry.addLine("building path 2");
		opmode.telemetry.update();
		lrr?.buildPath(name, 1);
		lrr?.getTrajectory()?.let { trajectory.add(it) };
		opmode.telemetry.clearAll();
		opmode.telemetry.addLine("building path 3");
		opmode.telemetry.update();
		lrr?.buildPath(name, 2);
		lrr?.getTrajectory()?.let { trajectory.add(it) };
		opmode.telemetry.clearAll();
		opmode.telemetry.addLine("done");
		opmode.telemetry.update();
	}
	
	fun startRR(name: String, recognition: Int = -1)
	{
		var r2 = recognition;
		if(r2 == -1)
		{
			r2 = LuaSettings.defultRecognition;
		}
		start(name, r2);
		lrr?.drive?.followTrajectorySequenceAsync(trajectory[r2]);
		val elapsedTime = ElapsedTime();
		var prevTime = 0.0f;
		while(lrr?.drive?.isBusy!!)
		{
			val deltaTime = elapsedTime.seconds() - prevTime;
			prevTime = elapsedTime.seconds().toFloat();
			lrr?.drive?.update();
			update(deltaTime.toFloat(), elapsedTime.seconds().toFloat());
			
		}
	}
	
	fun isRR(): Boolean
	{
		return trajectory.size > 0;
	}
	
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
		Log.e("Lua", msg);
		opmode.telemetry.clearAll();
		opmode.telemetry.addData("Lua Error", msg);
		opmode.telemetry.update();
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