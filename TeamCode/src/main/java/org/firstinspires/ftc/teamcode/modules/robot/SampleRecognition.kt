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

	var width = 0;
	var height = 0;

	class SampleColors(val low: Scalar, val high: Scalar, val low2: Scalar?, val high2: Scalar?)
	{
		companion object
		{
			val Red = SampleColors(
				Scalar(160.0, 150.0, 20.0),
				Scalar(180.0, 255.0, 255.0),
				Scalar(0.0, 150.0, 20.0),
				Scalar(20.0, 255.0, 255.0)
			);
			val Blue = SampleColors(Scalar(90.0, 150.0, 20.0), Scalar(125.0, 255.0, 255.0), null, null);
			val Red2 = SampleColors(Scalar(0.0, 0.0, 180.0), Scalar(160.0, 120.0, 255.0), null, null);
			val Blue2 = SampleColors(Scalar(130.0, 0.0, 0.0), Scalar(255.0, 120.0, 110.0), null, null);
			val Yellow = SampleColors(Scalar(0.0, 120.0, 180.0), Scalar(160.0, 255.0, 255.0), null, null);
		}
	}

	@Synchronized
	fun getSampleList(newList: ArrayList<Sample>?): ArrayList<Sample>
	{
		val list2 = sampleList;
		if(newList != null)
		{
			sampleList = newList;
		}
		return list2;
	}

	override fun init(width2: Int, height2: Int, calibration: CameraCalibration?)
	{
		width = width2;
		height = height2;
	}

	override fun processFrame(frame: Mat, captureTimeNanos: Long): Mat
	{
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2HSV);

		val inRange1 = Mat();
		Core.inRange(frame, sampleColors.low, sampleColors.high, inRange1);

		if(sampleColors.low2 != null)
		{
			val inRange2 = Mat();
			Core.inRange(frame, sampleColors.low2, sampleColors.high2, inRange2);

			Core.add(inRange1, inRange2, frame);
		}
		else
		{
			frame.copyTo(inRange1);
		}


		val countors = ArrayList<MatOfPoint>();
		Imgproc.findContours(
			frame, countors, MatOfPoint2f(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE
		);

		val sampleList2 = ArrayList<Sample>();

		for(countor in countors)
		{
			val countor2f = MatOfPoint2f();
			countor.convertTo(countor2f, CvType.CV_32F);
			val rectangle = Imgproc.minAreaRect(countor2f);
			if(rectangle.size.width > 50 && rectangle.size.height > 50)
			{
				val pos = Vec2(rectangle.center.x.toFloat(), rectangle.center.y.toFloat());
				if(rectangle.size.width > rectangle.size.height) rectangle.angle += 90;
				sampleList2.add(Sample(pos, rectangle.angle.toFloat()));
			}
		}

		getSampleList(sampleList2);

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