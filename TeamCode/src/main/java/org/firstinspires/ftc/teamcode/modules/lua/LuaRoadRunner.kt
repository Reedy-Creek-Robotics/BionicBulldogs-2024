package org.firstinspires.ftc.teamcode.modules.lua

import android.util.Log
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder

class LuaRoadRunner
{
	companion object
	{
		init
		{
			System.loadLibrary("ftcrobotcontroller");
		}
	}
	
	var drive: SampleMecanumDrive? = null;
	private var opmode: LinearOpMode? = null;
	private var builder: TrajectorySequenceBuilder? = null;
	
	constructor(opMode: LinearOpMode)
	{
		drive = SampleMecanumDrive(opMode.hardwareMap);
		opmode = opMode;
	}
	
	fun pathErr(msg: String)
	{
		throw PathError(msg);
	}
	
	fun getTrajectory(): TrajectorySequence?
	{
		return builder?.build();
	}
	
	fun makeBuilder(x: Double, y: Double, heading: Double)
	{
		builder = drive?.trajectorySequenceBuilder(Pose2d(x, y, Math.toRadians(heading)));
	}
	
	fun lineTo(x: Double, y: Double)
	{
		builder?.lineTo(Vector2d(x, y));
	}
	
	fun splineTo(x: Double, y: Double, heading: Double)
	{
		builder?.splineTo(Vector2d(x, y), Math.toRadians(heading));
	}
	
	fun lineToLinearHeading(x: Double, y: Double, heading: Double)
	{
		builder?.lineToLinearHeading(Pose2d(x, y, Math.toRadians(heading)));
	}
	
	fun splineToLinearHeading(x: Double, y: Double, heading: Double, angle: Double)
	{
		builder?.splineToLinearHeading(
			Pose2d(x, y, Math.toRadians(heading)),
			Math.toRadians(angle)
		);
	}
	
	fun lineToConstantHeading(x: Double, y: Double)
	{
		builder?.lineToConstantHeading(Vector2d(x, y));
	}
	
	fun splineToConstantHeading(x: Double, y: Double, heading: Double)
	{
		builder?.splineToConstantHeading(Vector2d(x, y), Math.toRadians(heading));
	}
	
	fun lineToSplineHeading(x: Double, y: Double, heading: Double)
	{
		builder?.lineToSplineHeading(Pose2d(x, y, Math.toRadians(heading)));
	}
	
	fun splineToSplineHeading(x: Double, y: Double, heading: Double, angle: Double)
	{
		builder?.splineToSplineHeading(
			Pose2d(x, y, Math.toRadians(heading)),
			Math.toRadians(angle)
		);
	}
	
	fun turn(ang: Double)
	{
		builder?.turn(ang);
	}
	
	fun wait(time: Double)
	{
		builder?.waitSeconds(time);
	}
	
	fun marker(string: String)
	{
		builder?.addDisplacementMarker {
			callDisplacement(string);
		};
	}
	
	private external fun callDisplacement(string: String);
	external fun buildPath(name: String, recognition: Int);
}