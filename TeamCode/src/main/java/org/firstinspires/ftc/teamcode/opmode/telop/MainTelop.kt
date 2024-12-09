package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.SampleOuttake
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class MainTelop: LinearOpMode()
{
	override fun runOpMode()
	{
		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		val specimenOuttake = SpecimenOuttake(claw, slide);
		val arm = Arm(hardwareMap.servo.get("arm"));
		val intake = Intake(
			hardwareMap.crservo.get("rotator0"), null, null
		);

		val outtake = Outtake(hardwareMap);

		val sampleOuttake = SampleOuttake(slide, outtake);

		val hSlide = HSlide(hardwareMap.servo.get("hslide"));

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		specimenOuttake.init();
		sampleOuttake.init();

		hSlide.zero();
		arm.down();

		//Controls:
		//Triggers retract/extend HSlides

		//Cross controls bucket scoring
		//Triangle loads specimen into bucket

		//Square raises/lowers intake
		//Right bumper toggles intake
		//Left bumper outtakes and then intakes again

		//Dpad up/down raises/lowers slides
		//Dpad left rotates bucket to position 1.0 (for some reason)
		//Dpad right rotates bucket to dump (position 0.9)

		var hangingState = 0;

		while(opModeIsActive())
		{
			gamepad.copy();

			//Drive
			drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

			//Horizontal Slides
			if(gamepad1.right_trigger >= 0.5 && hSlide.pos() >= hSlide.min())
			{
				hSlide.decrement();
			}
			else if(gamepad1.left_trigger >= 0.5 && hSlide.pos() <= hSlide.max())
			{
				hSlide.increment();
			}
			else if(gamepad.triangle())
			{
				hSlide.score();
				arm.up();
			}

			// Outtake
			if(gamepad.dpadLeft())
			{
				outtake.bucketScore();
			}
			if(gamepad.dpadUp())
			{
				outtake.bucketDown();
			}
			if(gamepad.dpadRight())
			{
				outtake.bucketUp();
			}

			if(outtake.update() == 1)
			{
				slide.lower();
			}

			//Specimine Claw
			if(gamepad.circle())
			{
				if(claw.state == SpeciminClaw.State.Closed)
				{
					claw.open();
				}
				else if(claw.state == SpeciminClaw.State.Open)
				{
					claw.close();
				}
			}

			// Intake
			if(gamepad.rightBumper())
			{
				if(intake.state == Intake.State.Forward)
				{
					intake.stop();
				}
				else
				{
					intake.forward();
				}
			}

			if(gamepad.leftBumper())
			{
				if(intake.state == Intake.State.Reverse)
				{
					intake.stop();
				}
				else
				{
					intake.outtakeForTime(1.5);
				}
			}

			// Outtake Slides
			if(gamepad.cross())
			{
				if(slide.state == Slide.State.Lower)
				{
					sampleOuttake.up();
				}
				else if(slide.state == Slide.State.Raise)
				{
					sampleOuttake.score();
				}
			}
			sampleOuttake.update();

			slide.update();

			// Specimen Outtake
      
			if(gamepad.touchpad())
			{
				if(specimenOuttake.state == SpecimenOuttake.State.Down)
				{
					specimenOuttake.collect();
				}
				else if(specimenOuttake.state == SpecimenOuttake.State.Up)
				{
					specimenOuttake.score();
				}
			}
			specimenOuttake.update();

			// Intake Arm
			if(gamepad.square())
			{
				if(arm.state == Arm.State.Up)
				{
					arm.down();
				}
				else if(arm.state == Arm.State.Down)
				{
					arm.up();
				}
			}

			if(gamepad.options())
			{
				if(hangingState == 0)
				{
					hangingState = 1;
					slide.gotoPos(-420);
				}
				else
				{
					slide.gotoPos(0);
					hangingState = 0;
				}
			}

			slide.telem(telemetry);
			drive.telem(telemetry);
			specimenOuttake.telem(telemetry);
			telemetry.addData("hPos", hSlide.pos())
			telemetry.addData("touchpad", gamepad1.touchpad);
			telemetry.update();
		}
	}

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}
