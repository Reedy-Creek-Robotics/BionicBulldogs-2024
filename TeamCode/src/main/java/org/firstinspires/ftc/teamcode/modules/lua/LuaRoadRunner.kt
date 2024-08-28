package org.firstinspires.ftc.teamcode.modules.lua

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.apache.commons.math3.geometry.euclidean.twod.Line
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence
import kotlin.collections.ArrayList

class LuaRoadRunner
{
	companion object
	{
		@JvmStatic
		var defaultRecognition: Int = 0;
	}
	
	val lua: Lua;
	private var opmodeName: String = "";
	private val telemetry: Telemetry;
	private val drive: SampleMecanumDrive;
	private val builder: LuaRoadRunnerBuilder;
	private val trajectories: ArrayList<TrajectorySequence> = ArrayList(3);
	
	private var luaInitilized = false;
	
	constructor(drive2: SampleMecanumDrive, opmode: LinearOpMode)
	{
		drive = drive2;
		builder = LuaRoadRunnerBuilder(drive);
		telemetry = opmode.telemetry;
		lua = Lua(opmode);
	}
	
	/**
	 * initalizes lua and returns the list of opmodes
	 * not required to be run
	 */
	fun initLua(): Array<String>
	{
		luaInitilized = true;
		return lua.init();
	}
	
	/**
	 * initalizes lua and builds all roadrunner paths
	 */
	fun init(name: String)
	{
		opmodeName = name;
		if(!luaInitilized)
			lua.init();
		
		internalInit(builder);
		
		telemetry.addLine("building path 1");
		telemetry.update();
		
		buildPath(name, 0);
		trajectories[0] = builder.getTrajectory();
		
		telemetry.addLine("building path 2");
		telemetry.update();
		
		buildPath(name, 1);
		trajectories[1] = builder.getTrajectory();
		
		telemetry.addLine("building path 3");
		telemetry.update();
		
		buildPath(name, 2);
		trajectories[2] = builder.getTrajectory();
		
		telemetry.addLine("done");
		telemetry.update();
	}
	
	fun start(recognition: Int = -1)
	{
		var r2 = recognition;
		if(r2 == -1)
		{
			r2 = defaultRecognition;
		}
		lua.start(opmodeName, r2);
		drive.followTrajectorySequenceAsync(trajectories[r2]);
		val elapsedTime = ElapsedTime();
		var prevTime = 0.0f;
		while(drive.isBusy)
		{
			val deltaTime = elapsedTime.seconds() - prevTime;
			prevTime = elapsedTime.seconds().toFloat();
			drive.update();
			lua.update(deltaTime.toFloat(), elapsedTime.seconds().toFloat());
		}
	}
	
	private external fun buildPath(name: String, recognition: Int);
	private external fun internalInit(builder: LuaRoadRunnerBuilder);
}