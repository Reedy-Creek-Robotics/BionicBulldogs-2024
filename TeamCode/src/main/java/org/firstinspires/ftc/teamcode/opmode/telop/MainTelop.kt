package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.opmode.config.*

@TeleOp
class MainTelop: LinearOpMode()
{
	enum class ScoreState
	{
		Up, Down
	}

	override fun runOpMode()
	{

		//Claw, slides (to pos), drive, eTake
		val claw = Claw(hardwareMap.servo.get("claw"));
		val slide = Slide(hardwareMap.dcMotor.get("slide"));
		val grip = Gripper(hardwareMap.servo.get("gripper"));
		val arm = Arm(hardwareMap.servo.get("arm"));
		val rotate = Spin(hardwareMap.crservo.get("rotator0"), hardwareMap.crservo.get("rotator1"))
		val hSlide = HSlide(hardwareMap.servo.get("hslide"));

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		slide.reverse();

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		hSlide.zero();
		claw.close();
		arm.up();

		var scoreState = ScoreState.Down;

		var count = 0;

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

			//hSlide
			if (gamepad1.right_trigger >= 0.5 && hSlide.pos() <= hSlide.max()) {
				hSlide.increment();
			} else if (gamepad1.left_trigger >= 0.5 && hSlide.pos() >= hSlide.min()) {
				hSlide.decrement();
			} else if (gamepad.triangle()) {
				hSlide.zero();
			}

			//Grip
			if(gamepad.dpadRight() || gamepad.dpadDown() || gamepad.dpadLeft() || gamepad.dpadUp())
			{
				if (grip.state != Gripper.State.Open)
				{
					grip.open();
				} else {
					grip.close();
				}
			};

			//Claw
			if(gamepad.circle())
			{
				if(claw.state == Claw.State.Closed)
				{
					claw.open();
				}
				else if(claw.state == Claw.State.Open)
				{
					claw.close();
				}
			}

			//Rotate rBumper
			if(gamepad.rightBumper())
			{
				if(rotate.state == Spin.State.Forward)
				{
					rotate.stop();
				}
				else
				{
					rotate.forward();
				}
			}

			//Rotate lBumper
			if(gamepad.leftBumper())
			{
				if(rotate.state == Spin.State.Reverse)
				{
					rotate.stop();
				}
				else
				{
					rotate.reverse();
				}
			}

			//Slide
			if(gamepad.cross())
			{
				if(slide.state == Slide.State.Raise)
				{
					slide.lower();
				}
				else if(slide.state == Slide.State.Lower)
				{
					slide.raise();
				}
			}

			//Combo
			if(gamepad.touchpad())
			{
				drive.drive(0.0f, 0.0f, 0.0f);
				if(scoreState == ScoreState.Down)
				{
					claw.close();
					delay(0.5);
					slide.raise();
					scoreState = ScoreState.Up;
				}
				else
				{
					slide.runToPosition(850);
					while(slide.busy());
					delay(0.5);
					claw.open();
					delay(0.5);
					slide.lower();
					scoreState = ScoreState.Down;
				}
			}

			//Arm
			if(gamepad.square())
			{
				count++;
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
			telemetry.addData("square count", count);
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