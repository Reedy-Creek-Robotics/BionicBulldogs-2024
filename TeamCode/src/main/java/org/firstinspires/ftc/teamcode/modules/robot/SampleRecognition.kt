package org.firstinspires.ftc.teamcode.modules.robot

import android.util.Log
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
		if(sample.res == sample2)
			continue;
		if(pos.y > sample.pos.y)
			continue;
		if(pos.x > sample.pos.x + 3)
			continue;
		if(pos.x < sample.pos.x - 3)
			continue;
		if(closestDist.value > dist)
			closestDist.value = dist;
	}
}

class SampleRecognition(private val telem: Telemetry?)
{
	companion object
	{
		val RED = 2;
		val BLUE = 1;
		val YELLOW = 0;
	}

	lateinit var limelight: Limelight3A;

	constructor(hardwareMap: HardwareMap, telem: Telemetry? = null): this(telem)
	{
		limelight = hardwareMap.get(Limelight3A::class.java, "limelight");
		limelight.setPollRateHz(100);
		limelight.start();
	}

	fun findBestSample(targetColor: Int): Sample
	{
		Log.d("sampleRecognition", "recognizing target");
		val targetResult = getSamples(targetColor);
		telemetry?.clearAll();
		telemetry?.fmt("--- Found Col %d ---", targetColor);
		telemetry?.update();

		Log.d("sampleRecognition", "recognizing other");
		var otherColorId = targetColor + 1;
		if(otherColorId > 2)
			otherColorId = 0;
		val otherColor = getSamples(targetColor);
		telemetry?.clearAll();
		telemetry?.fmt("--- Found Col %d ---", targetColor);
		telemetry?.fmt("--- Found Col %d ---", otherColorId);
		telemetry?.update();

		Log.d("sampleRecognition", "recognizing other2");
		var otherColorId2 = otherColorId + 1;
		if(otherColorId2 > 2)
			otherColorId2 = 0;
		val otherColor2 = getSamples(otherColorId2);
		telemetry?.clearAll();
		telemetry?.fmt("--- Found Col %d ---", targetColor);
		telemetry?.fmt("--- Found Col %d ---", otherColorId);
		telemetry?.fmt("--- Found Col %d ---", otherColorId2);
		telemetry?.update();
		Log.d("sampleRecognition", "recognition done");

		Log.d("sampleRecognition", "target %d".format(targetResult.size));
		Log.d("sampleRecognition", "target %d".format(otherColor.size));
		Log.d("sampleRecognition", "target %d".format(otherColor2.size));

		telemetry?.clearAll();
		telemetry?.fmt("Red %d", otherColor.size);
		telemetry?.fmt("Blue %d", otherColor2.size);
		telemetry?.fmt("Yellow %d", targetResult.size);

		val samples = ArrayList<Sample>();
		val allSamples = ArrayList<Sample>();
		for(res: LLResultTypes.ColorResult in targetResult)
		{
			val pos = getSamplePosition(res);
			telemetry?.fmt("--- sample x: %f, y: %f ---", pos.x, pos.y);
			val sample = Sample();
			sample.res = res;
			sample.pos = pos;
			val dist = FloatPtr(9999.0f);

			processSampleList(otherColor, sample, dist);
			processSampleList(otherColor2, sample, dist);
			processSampleList(targetResult, sample, dist);

			sample.dist = dist.value;
			allSamples.add(sample);

			if(sample.pos.y > 32)
			{
				telemetry?.addLine("sample too far forward, skipping");
				continue;
			}

			if(dist.value < 3)
			{
				telemetry?.addLine("sample too close to other samples, skipping");
				continue;
			}

			samples.add(sample);
		}

		samples.sortWith({a, b -> (a.dist - b.dist).toInt()});

		val maxSample = if(samples.size > 0)
			samples[0];
		else
			allSamples[0];


		if(telemetry != null)
		{
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
