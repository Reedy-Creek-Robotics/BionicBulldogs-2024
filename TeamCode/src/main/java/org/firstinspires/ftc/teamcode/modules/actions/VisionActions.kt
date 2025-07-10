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
		sample2 = sampleRecognition.findBestSample(SampleRecognition.YELLOW);
		return false;
	}
}

class VisionAction_MoveToSample(private val xOff: Float = 0.0f, private val yOff: Float = 0.0f): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		val sample = sample2 ?: throw LuaError("tried to move to a null sample");

		val tx = -sample.pos.x - 5.5f;

		val tangent = Math.toRadians(if(sample.pos.x > 0) -90.0 else 90.0);
		val trajectory = drive.actionBuilder(drive.localizer.pose)
			.setTangent(tangent)
			.splineToConstantHeading(Vector2d(xOff.toDouble(), tx + yOff.toDouble()), tangent)
			.build();

		runBlocking(trajectory);
		return false;
	}
}

class VisionAction_ExtendHslideToSample(private val offset: Float = 0.0f): Action
{
	override fun run(p: TelemetryPacket): Boolean
	{
		val sample = sample2 ?: throw LuaError("tried to extend slides to a null sample");
		val hslidePos = (HSlide.min - HSlide.max) * ((sample.pos.y - 5) / 21.0) + HSlide.max;
		hSlide.gotoPos(hslidePos)
		return false;
	}
}