package org.firstinspires.ftc.teamcode.modules.robot

import android.graphics.Canvas
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.teamcode.modules.Vec2
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

class SampleRecognition(private val sampleColors: SampleColors): VisionProcessor
{
	private var sampleList = ArrayList<Sample>();

	class SampleColors(val low: Scalar, val high: Scalar)
	{
		companion object
		{
			val Red = SampleColors(Scalar(0.0, 0.0, 180.0), Scalar(160.0, 120.0, 255.0));
			val Blue = SampleColors(Scalar(130.0, 0.0, 0.0), Scalar(255.0, 120.0, 110.0));
			val Yellow = SampleColors(Scalar(0.0, 120.0, 180.0), Scalar(160.0, 255.0, 255.0));
		}
	}

	fun getSampleList(): ArrayList<Sample>
	{
		return sampleList;
	}

	override fun init(width: Int, height: Int, calibration: CameraCalibration?)
	{
	}

	override fun processFrame(frame: Mat, captureTimeNanos: Long): Mat
	{
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2BGR);

		Core.inRange(frame, sampleColors.low, sampleColors.high, frame);

		var countors = ArrayList<MatOfPoint>();
		Imgproc.findContours(frame, countors, MatOfPoint2f(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

		val sampleList2 = ArrayList<Sample>();

		for(countor in countors)
		{
			var countor2f = MatOfPoint2f();
			countor.convertTo(countor2f, CvType.CV_32F);
			val rectangle = Imgproc.minAreaRect(countor2f);
			val pos = Vec2(rectangle.center.x.toFloat(), rectangle.center.y.toFloat());
			sampleList2.add(Sample(pos, rectangle.angle.toFloat()));
		}

		sampleList = sampleList2;

		return frame;
	}

	override fun onDrawFrame(
		canvas: Canvas?,
		onscreenWidth: Int,
		onscreenHeight: Int,
		scaleBmpPxToCanvasPx: Float,
		scaleCanvasDensity: Float,
		userContext: Any?
	)
	{
	}
}