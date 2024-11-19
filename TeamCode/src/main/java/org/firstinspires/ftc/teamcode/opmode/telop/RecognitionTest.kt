package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.modules.clamp
import org.firstinspires.ftc.teamcode.modules.robot.SampleRecognition
import org.firstinspires.ftc.vision.VisionPortal
import kotlin.math.pow
import kotlin.math.sqrt

@TeleOp
class RecognitionTest: LinearOpMode()
{
	override fun runOpMode()
	{
//		val servo = hardwareMap.servo.get("servo");

		val processor = SampleRecognition(SampleRecognition.SampleColors.Red);

		val visionPortalBuilder = VisionPortal.Builder();
		visionPortalBuilder.addProcessor(processor);
		visionPortalBuilder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"));
		val visionPortal = visionPortalBuilder.build();

		waitForStart();

		while(opModeIsActive())
		{
			val sampleList = processor.getSampleList(null);

			var closestDist = -1.0f;
			var closestInd = -1;

			for((k, sample) in sampleList.withIndex())
			{
				val dist = sqrt(
					(sample.position.x - (processor.width / 2)).pow(2) + (sample.position.y - processor.height).pow(
						2
					)
				);
				if(dist < closestDist || closestDist == -1.0f)
				{
					closestDist = dist;
					closestInd = k;
				}
			}

			if(closestInd > 0)
			{
				val closest = sampleList[closestInd];
				telemetry.addData("closestSample", closestInd);
				telemetry.addLine(
					String.format(
						"closest x %i, y %i, angle %i",
						closest.position.x,
						closest.position.y,
						closest.rotation
					)
				);
			}
			else
			{
				telemetry.addLine("no samples found");
			}

			for((i, sample) in sampleList.withIndex())
			{
				telemetry.addData("sample $i position::x", sample.position.x);
				telemetry.addData("sample $i position::y", sample.position.y);
				telemetry.addData("sample $i rotation", sample.rotation);
			}
			telemetry.update();
//			if(sampleList.size > 0)
//			{
//				servo.position = (clamp(sampleList[0].rotation + 180, 0.0f, 180.0f) / 180).toDouble();
//			}
		}
	}
}