package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.modules.robot.Sample
import org.firstinspires.ftc.teamcode.modules.robot.SampleRecognition
import org.firstinspires.ftc.vision.VisionPortal

@TeleOp
class RecognitionTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val processor = SampleRecognition(SampleRecognition.SampleColors.Red);

		val visionPortalBuilder = VisionPortal.Builder()
		visionPortalBuilder.addProcessor(processor)
		visionPortalBuilder.setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"));
		val visionPortal = visionPortalBuilder.build();

		waitForStart();

		while(opModeIsActive())
		{
			val sampleList = processor.getSampleList();
			for((i, sample) in sampleList.withIndex())
			{
				telemetry.addData("sample $i position::x", sample.position.x);
				telemetry.addData("sample $i position::y", sample.position.y);
				telemetry.addData("sample $i rotation", sample.rotation);
			}
			telemetry.update();
		}
	}
}