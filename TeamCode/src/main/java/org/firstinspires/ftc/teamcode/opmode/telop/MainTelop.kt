package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
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

@Config
@TeleOp
class MainTelop: LinearOpMode()
{
	companion object
	{
		@JvmField
		var hangingHeightDown = 0;

		@JvmField
		var hangingHeightUp = -1200;
	}

	override fun runOpMode()
	{
		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		val specimenOuttake = SpecimenOuttake(claw, slide);
		val arm = Arm(hardwareMap);
		val intake = Intake(hardwareMap);

		val outtake = Outtake(hardwareMap);

		val sampleOuttake = SampleOuttake(slide, outtake);

		val hSlide = HSlide(hardwareMap);

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		drive.setPosEstimate(Pose2d(0.0, 0.0, rotPos));

		var localHeading = 0.0;
		var imuHeading = 0.0;

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		specimenOuttake.init();
		sampleOuttake.init();

		hSlide.zero();
		arm.up();

		intake.zeroRotator();

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

			if(gamepad.share())
			{
				val imu = hardwareMap.get(SparkFunOTOS::class.java, "imu2");
				val localizer = SparkfunImuLocalizer(imu);
				drive.setLocalizer(localizer);
				localizer.update();
				imuHeading = imu.position.h;
				localHeading = localizer.poseEstimate.heading.toDouble();
			}

			//Horizontal Slides
			if(gamepad1.right_trigger >= 0.5 && hSlide.pos() <= hSlide.min())
			{
				hSlide.increment();
			}
			else if(gamepad1.left_trigger >= 0.5 && hSlide.pos() >= hSlide.max())
			{
				hSlide.decrement();
			}
			else if(gamepad.triangle())
			{
				hSlide.score();
				arm.up();
				intake.zeroRotator();
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

			if(gamepad.dpadDown())
			{
				hSlide.gotoPos(0.48);
				arm.gotoPos(0.0);
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
					intake.stopIn(0.75);
				}
			}

			if(gamepad.leftStick())
			{
				intake.rotatorLeft();
			}

			if(gamepad.rightStick())
			{
				intake.rotatorRight();
			}

			intake.update();

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
					slide.gotoPos(hangingHeightUp);
				}
				else
				{
					slide.gotoPos(hangingHeightDown);
					hangingState = 0;
				}
			}

			/*Stik
			if (gamepad.square()) {
				if (stik.state == Stik.State.Raised) {
					stik.raise();
					stikRotate.outake();
				} else if (stik.state == Stik.State.Lowered) {
					stik.lower();
					stikRotate.intake();
				}
			}
			if (gamepad.circle()) {
				if (stikClaw.state == StikClaw.State.Closed) {
					stikClaw.open();
				} else if (stikClaw.state == StikClaw.State.Opened) {
					stikClaw.close();
				}
			}*/

			telemetry.addData("imu heading", imuHeading);
			telemetry.addData("localizer heading", localHeading);
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
