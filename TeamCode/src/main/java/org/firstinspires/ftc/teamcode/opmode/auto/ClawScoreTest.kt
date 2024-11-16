package org.firstinspires.ftc.teamcode.opmode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.robot.Claw
import org.firstinspires.ftc.teamcode.modules.robot.Slides

@Autonomous
class ClawScoreTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val claw = Claw(hardwareMap.servo.get("claw"));

		val slides = Slides(hardwareMap.dcMotor.get("slide"));

		val topPos = -1300;

		waitForStart();

		claw.close();

		sleep(1000);

		slides.runToPosition(topPos);

		while(slides.isBusy());

		sleep(1000);

		slides.runToPosition(-850);
		while(slides.isBusy());

		claw.open();
		sleep(1000);
	}
}