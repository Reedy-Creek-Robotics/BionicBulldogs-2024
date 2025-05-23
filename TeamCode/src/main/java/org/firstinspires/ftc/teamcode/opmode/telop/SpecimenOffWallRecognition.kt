package org.firstinspires.ftc.teamcode.opmode.telop

import android.graphics.Canvas
import android.graphics.Paint

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

import org.opencv.core.Mat
import org.opencv.core.Rect

import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.VisionProcessor
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration

class Processor : VisionProcessor
{
  override fun init(width: Int, height: Int, callibration: CameraCalibration)
  {
  }
  
  override fun processFrame(frame: Mat, frameTime: Long): Mat
  {
    val width = frame.width();
    val height = frame.height();
    val start = height / 3;
    var frame2 = Mat(frame, Rect(0, start, width, height - start));

    return frame2;
  }

  override fun onDrawFrame(canvas: Canvas?, width: Int, height: Int, scale: Float, density: Float, context: Any?)
  {
  }
}

@TeleOp
class SpecimenOffWallRecognition : LinearOpMode()
{
  override fun runOpMode()
  {
		val builder = VisionPortal.Builder();

		builder.setCamera(
			hardwareMap.get(
				WebcamName::class.java,
				"Webcam 1"
			)
		);

    val processor = Processor();

		builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
    builder.addProcessor(processor);
		builder.enableLiveView(true);
		builder.setAutoStopLiveView(true);

		val visonPortal = builder.build();

		waitForStart();
		while(opModeIsActive());
  }
}
