package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.modules.Vec2
import org.firstinspires.ftc.teamcode.modules.fmt
import org.firstinspires.ftc.teamcode.modules.ui.FloatPtr
import org.firstinspires.ftc.teamcode.opmode.telop.Sample
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

private var telemetry: Telemetry? = null;

class Sample
{
	lateinit var res: LLResultTypes.ColorResult;
	lateinit var pos: Vec2;
	var dist = 0.0f;
	var width = 0.0f;
	var height = 0.0f;
}

fun getSamplePosition(sample: LLResultTypes.ColorResult): Vec2
{
	return getSamplePosition(sample.targetXDegrees.toFloat(), sample.targetYDegrees.toFloat());
}

fun getSamplePosition(tx: Float, ty: Float): Vec2
{
	val a1 = -25 * PI / 180;
	val a2 = ty * PI / 180;
	val dy = -13.75 / tan(a1 + a2);

	val a3 = 0 * PI / 180;
	val a4 = tx * PI / 180;
	val dx = dy * tan(a3 + a4);
	return Vec2(dx.toFloat(), dy.toFloat());
}

private fun processSampleList(
	list: List<LLResultTypes.ColorResult>,
	sample: Sample,
	closestDist: FloatPtr
)
{
	for(sample2 in list)
	{
		val pos = org.firstinspires.ftc.teamcode.opmode.telop.getSamplePosition(sample2);
		val dist = sqrt((pos.x - sample.pos.x).pow(2) + (pos.y - sample.pos.y).pow(2));
		telemetry?.addLine("sample2 x: ${pos.x}, y: ${pos.y}, d: $dist");
		if(sample.res == sample2)
		{
			telemetry?.addLine("sample same as target, skipping");
			continue;
		}
		if(pos.y > sample.pos.y)
		{
			telemetry?.addLine("sample behind target, skipping");
			continue;
		}
		if(pos.x > sample.pos.x + 3)
		{
			telemetry?.addLine("sample too far right, skipping");
			continue;
		}
		if(pos.x < sample.pos.x - 3)
		{
			telemetry?.addLine("sample too far left, skipping");
			continue;
		}
		if(closestDist.value > dist)
		{
			telemetry?.addLine("dist updated to $dist");
			closestDist.value = dist;
		}
	}
}

class SampleRecognition(hardwareMap: HardwareMap, private val telem: Telemetry? = null)
{
	companion object
	{
		val RED = 2;
		val BLUE = 1;
		val YELLOW = 0;
	}

	val limelight = hardwareMap.get(Limelight3A::class.java, "limelight");
	fun findBestSample(targetColor: Int): Sample
	{
		val targetResult = getSamples(targetColor);
		telemetry?.clearAll();
		telemetry?.fmt("--- Found Col %d ---", targetColor);
		telemetry?.update();

		var otherColorId = targetColor + 1;
		if(otherColorId > 2)
			otherColorId = 0;
		val otherColor = getSamples(targetColor);
		telemetry?.clearAll();
		telemetry?.fmt("--- Found Col %d ---", targetColor);
		telemetry?.fmt("--- Found Col %d ---", otherColorId);
		telemetry?.update();

		var otherColorId2 = otherColorId + 1;
		if(otherColorId2 > 2)
			otherColorId2 = 0;
		val otherColor2 = getSamples(otherColorId2);
		telemetry?.clearAll();
		telemetry?.fmt("--- Found Col %d ---", targetColor);
		telemetry?.fmt("--- Found Col %d ---", otherColorId);
		telemetry?.fmt("--- Found Col %d ---", otherColorId2);
		telemetry?.update();

		telemetry?.clearAll();
		telemetry?.fmt("Red %d", otherColor.size);
		telemetry?.fmt("Blue %d", otherColor2.size);
		telemetry?.fmt("Yellow %d", targetResult.size);

		val samples = ArrayList<Sample>();
		for(res: LLResultTypes.ColorResult in targetResult)
		{
			val pos = getSamplePosition(res);
			telemetry?.fmt("--- sample x: %f, y: %f ---", pos.x, pos.y);
			val sample = Sample();
			sample.res = res;
			sample.pos = pos;
			if(res.targetYDegrees < -14)
			{
				telemetry?.addLine("sample too low, skipping");
				continue;
			}
			val dist = FloatPtr(9999.0f);

			telemetry?.addLine("--- Red List ---");
			processSampleList(otherColor, sample, dist);
			telemetry?.addLine("--- Blue List ---");
			processSampleList(otherColor2, sample, dist);
			telemetry?.addLine("--- Yellow List ---");
			processSampleList(targetResult, sample, dist);

			sample.dist = dist.value;
			samples.add(sample);
		}

		samples.sortWith({a, b -> (a.dist - b.dist).toInt()});

		val maxSample = samples[0];

		if(telemetry != null)
		{
			val dist = FloatPtr(999999.0f);
			telemetry?.addLine("--- Closest Sample ---");
			telemetry?.addLine("--- Red List ---");
			processSampleList(otherColor, maxSample, dist);
			telemetry?.addLine("--- Blue List ---");
			processSampleList(otherColor2, maxSample, dist);
			telemetry?.addLine("--- Yellow List ---");
			processSampleList(targetResult, maxSample, dist);
			telemetry?.addLine("--- Closest Sample ---");
			telemetry?.fmt("dist: %f", maxSample.dist);
			telemetry?.fmt("x: %f", maxSample.pos.x);
			telemetry?.fmt("y: %f", maxSample.pos.y);
			telemetry?.fmt("tx: %f", maxSample.res.targetXDegrees);
			telemetry?.fmt("ty: %f", maxSample.res.targetYDegrees);
			telemetry?.update();
		}
		return maxSample;
	}

	private fun getSamples(color: Int): List<LLResultTypes.ColorResult>
	{
		limelight.pipelineSwitch(color);
		var result = limelight.latestResult;
		while(result == null || !result.isValid || result.pipelineIndex != color)
			result = limelight.latestResult;
		return result.colorResults;
	}
}
