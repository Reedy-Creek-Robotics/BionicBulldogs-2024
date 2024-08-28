package org.firstinspires.ftc.teamcode.modules.lua

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence

class LuaRoadRunner(drive2: SampleMecanumDrive, opmode: LinearOpMode)
{
	val lua: Lua;
	private var opmodeName: String = "";
	private val telemetry: Telemetry;
	private val drive: SampleMecanumDrive = drive2;
	private val builder: LuaRoadRunnerBuilder = LuaRoadRunnerBuilder(drive);
	private val trajectories: java.util.ArrayList<TrajectorySequence> = java.util.ArrayList(3);

	private var luaInitilized = false;

	init
	{
		telemetry = opmode.telemetry
		lua = Lua(opmode)
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
		if(!luaInitilized) lua.init();

		internalInit(builder);

		telemetry.addLine("building path 1");
		telemetry.update();

		buildPath(name, 0);
		trajectories.add(builder.getTrajectory());

		telemetry.addLine("building path 2");
		telemetry.update();

		buildPath(name, 1);
		trajectories.add(builder.getTrajectory());

		telemetry.addLine("building path 3");
		telemetry.update();

		buildPath(name, 2);
		trajectories.add(builder.getTrajectory());

		telemetry.addLine("done");
		telemetry.update();

		close();
	}

	fun start(recognition: Int = -1)
	{
		var r2 = recognition;
		if(r2 == -1)
		{
			r2 = Lua.defaultRecognition;
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
	private external fun close();
}