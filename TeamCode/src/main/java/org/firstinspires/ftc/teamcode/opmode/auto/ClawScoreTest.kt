package org.firstinspires.ftc.teamcode.opmode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.modules.robot.Slide

@Autonomous
class ClawScoreTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val speciminClaw = SpeciminClaw(hardwareMap.servo.get("claw"));

		val slides = Slide(hardwareMap.dcMotor.get("slide") as DcMotorEx);

		val topPos = -1300;

		waitForStart();

		speciminClaw.close();

		sleep(1000);

		slides.runToPosition(topPos);

		while(slides.busy());

		sleep(1000);

		slides.runToPosition(-850);
		while(slides.busy());

		speciminClaw.open();
		sleep(1000);
	}
}