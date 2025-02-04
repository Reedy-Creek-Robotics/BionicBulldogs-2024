package org.firstinspires.ftc.teamcode.opmode.telop

import android.graphics.Color
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.opencv.ImageRegion
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor

@TeleOp
class RecognitionTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val processor = PredominantColorProcessor.Builder()
			.setRoi(ImageRegion.entireFrame())
			.setSwatches(
				PredominantColorProcessor.Swatch.RED,
				PredominantColorProcessor.Swatch.BLUE,
				PredominantColorProcessor.Swatch.YELLOW,
				PredominantColorProcessor.Swatch.BLACK,
				PredominantColorProcessor.Swatch.WHITE)
			.build();

		val visionPortal = VisionPortal.Builder()
			.addProcessor(processor)
			.setCameraResolution(android.util.Size(640, 480))
			.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
			.build();

		waitForStart();

		while(opModeIsActive() || opModeInInit()) {

			val result = processor.analysis;


			/*if(sampleList.size > 0)
			{
				servo.position = (clamp(sampleList[0].rotation + 180, 0.0f, 180.0f)/* / 180*/).toDouble();
			}*/
			//telemetry.addData("pos", servo.position)

			telemetry.addData("Best Match:", result.closestSwatch);
			telemetry.addData("R", Color.red(result.rgb));
			telemetry.addData("G", Color.green(result.rgb));
			telemetry.addData("B", Color.blue(result.rgb));
			telemetry.update();
		}
	}
}