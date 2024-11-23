package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class MainTelop: LinearOpMode()
{
	enum class ScoreState
	{
		Up, Down, Lowering
	}

	override fun runOpMode()
	{
		//Claw, slides (to pos), drive, eTake
		val speciminClaw = SpeciminClaw(hardwareMap.servo.get("claw"));
		val slide = Slide(hardwareMap.dcMotor.get("slide") as DcMotorEx);
//		val griper = Gripper(hardwareMap.servo.get("gripper"));
		val arm = Arm(hardwareMap.servo.get("arm"));
		val intake = Intake(
			hardwareMap.crservo.get("rotator0"), null, null
		);

		val outtake = Outtake(hardwareMap);

		val hSlide = HSlide(hardwareMap.servo.get("hslide"));

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));
		drive.setPosEstimate(Pose2d(0.0, 0.0, 0.0));

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		speciminClaw.close();
		hSlide.zero();
		arm.down();

		outtake.armDown();
		outtake.bucketDown();

		var scoreState = ScoreState.Down;

		//Controls:
		//Claw - Circle toggles open/close
		//gripTake (rotate) - rBumper toggles intake (forward), lBumper toggles intake (reverse)
		//Slide - Set to position, cross for toggling
		//Combo - Touchpad = Closes claw + raises slides
		//HSlide - Rtrig increments; Ltrig decrements, Triangle zeroes
		//Grip - Dpad (temporarily)
		//Arm - Square toggles up/down

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

			if(gamepad.dpadUp())
			{
				outtake.up();
			}
			if(gamepad.dpadDown())
			{
				outtake.armDown();
				outtake.bucketDown()
			}
			if(gamepad.dpadLeft())
			{
				outtake.bucketScore();
			}
			if(gamepad.dpadRight())
			{
				outtake.bucketUp();
			}

			if(outtake.update() == 1)
			{
				slide.lower();
			}

			//Grip
			/*if(gamepad.dpadDown() || gamepad.dpadUp())
			{
				if(gripper.state != Gripper.State.Open)
				{
					gripper.open();
				}
				else
				{
					gripper.close();
				}
			}*/

			/*if(gamepad1.dpad_left)
			{
				if(rotatorPosition > 0)
				{
					rotatorPosition += 0.1;
					rotator0.power = rotatorPosition;
				}
			}

			if(gamepad1.dpad_right)
			{
				if(rotatorPosition < 1)
				{
					rotatorPosition -= 0.1;
					rotator0.power = rotatorPosition;
				}
			}*/

			//Specimine Claw

			if(gamepad.circle())
			{
				if(speciminClaw.state == SpeciminClaw.State.Closed)
				{
					speciminClaw.open();
				}
				else if(speciminClaw.state == SpeciminClaw.State.Open)
				{
					speciminClaw.close();
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
					intake.outtakeForTime(1.0);
				}
			}

			intake.update();

			// Outtake Slides

			if(gamepad.cross())
			{
				if(slide.state == Slide.State.Raise)
				{
					outtake.score();
				}
				else if(slide.state == Slide.State.Lower)
				{
					slide.gotoPos(-2500);
					outtake.up();
				}
			}

			slide.update();

			//Combo

			if(gamepad.touchpad())
			{
				drive.drive(0.0f, 0.0f, 0.0f);
				if(scoreState == ScoreState.Down)
				{
					speciminClaw.close();
					delay(0.5);
					slide.raise();
					scoreState = ScoreState.Up;
				}
				else
				{
					slide.lower();
					scoreState = ScoreState.Lowering;
				}
			}

			if(scoreState == ScoreState.Lowering)
			{
				if(slide.getPos() > -1000)
				{
					speciminClaw.open();
					scoreState = ScoreState.Down;
				}
			}

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
			slide.telem(telemetry);
			drive.telem(telemetry);
			telemetry.addData("hPos", hSlide.pos())
			telemetry.addData("square", gamepad.square());
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