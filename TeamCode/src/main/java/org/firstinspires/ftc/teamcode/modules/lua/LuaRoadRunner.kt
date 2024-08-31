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

	init
	{
		telemetry = opmode.telemetry
		lua = Lua(opmode)
	}

	/**
	 * initalizes lua and returns the list of opmodes
	 * not required to be run
	 */
	fun init(): Array<String>
	{
		return lua.init();
	}

	/**
	 * initalizes lua and builds all roadrunner paths
	 */
	fun loadOpmode(name: String)
	{
		opmodeName = name;

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

	fun start()
	{
		start(Lua.defaultRecognition);
	}
	
	fun start(recognition: Int)
	{
		drive.poseEstimate = trajectories[recognition].start();
		lua.start(opmodeName, recognition);
		drive.followTrajectorySequenceAsync(trajectories[recognition]);
		val elapsedTime = ElapsedTime();
		var prevTime = 0.0;
		while(drive.isBusy)
		{
			val curTime = elapsedTime.seconds();
			val deltaTime = curTime - prevTime;
			prevTime = curTime;
			drive.update();
			lua.update(deltaTime, curTime);
		}
	}

	private external fun buildPath(name: String, recognition: Int);
	private external fun internalInit(builder: LuaRoadRunnerBuilder);
	private external fun close();
}