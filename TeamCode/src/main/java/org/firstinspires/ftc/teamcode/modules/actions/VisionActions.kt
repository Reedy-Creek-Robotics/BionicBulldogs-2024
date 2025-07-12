package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.*
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.minerkid08.dynamicopmodeloader.LuaError
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.SampleRecognition
import org.firstinspires.ftc.teamcode.opmode.telop.Sample

private var sample2: Sample? = null;

class VisionAction_RecognizeSample: Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		android.util.Log.d("recognizeSample", "start");
		sample2 = sampleRecognition.findBestSample(SampleRecognition.YELLOW);
		android.util.Log.d("recognizeSample", "end");
		return false;
	}
}

class VisionAction_MoveToSample(private val xOff: Float = 0.0f, private val yOff: Float = 0.0f, private val tangent: Float = 0.0f): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		android.util.Log.d("moveToSample", "start");
		val sample = sample2 ?: throw LuaError("tried to move to a null sample");

		val tx = -sample.pos.x - 5.5f;

		val tangent = Math.toRadians(tangent.toDouble());
		android.util.Log.d("moveToSample", "build trajectory");
		android.util.Log.d("moveToSample", "moving from ${drive.localizer.pose.position.x}, ${drive.localizer.pose.position.y}, ${drive.localizer.pose.heading.toDouble()}");
		android.util.Log.d("moveToSample", "moving to ${xOff}, ${yOff + tx}, ${drive.localizer.pose.heading.toDouble()}");
		val trajectory = drive.actionBuilder(drive.localizer.pose)
			.setTangent(tangent)
			.splineToLinearHeading(Pose2d(xOff.toDouble(), tx + yOff.toDouble(), 0.0), tangent)
			.build();

		android.util.Log.d("moveToSample", "run trajectory");
		runBlocking(trajectory);
		android.util.Log.d("moveToSample", "end");
		return false;
	}
}

class VisionAction_ExtendHslideToSample(private val offset: Float = 0.0f): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		android.util.Log.d("extendHslideToSample", "start");
		val sample = sample2 ?: throw LuaError("tried to extend slides to a null sample");
		val hslidePos = (HSlide.min - HSlide.max) * ((sample.pos.y - 5 + offset) / 21.0) + HSlide.max;
		hSlide.gotoPos(hslidePos)
		android.util.Log.d("extendHslideToSample", "end");
		return false;
	}
}