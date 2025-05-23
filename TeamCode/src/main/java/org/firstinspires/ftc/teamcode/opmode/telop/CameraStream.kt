package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal

@TeleOp
class CameraStream : LinearOpMode()
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

		builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
		builder.enableLiveView(true);
		builder.setAutoStopLiveView(true);

		val visonPortal = builder.build();

		waitForStart();
		while(opModeIsActive());
	}
}
