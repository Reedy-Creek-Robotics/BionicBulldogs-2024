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
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@Config
@TeleOp
class OuttakeTelop: LinearOpMode()
{
	companion object
	{
		@JvmField
		var clawOpen = 0.6;

		@JvmField
		var clawClose = 0.97;

		@JvmField
		var armDown = 0.9;

		@JvmField
		var armUp = 0.3;

		@JvmField
		var armUp2 = 0.85;

		@JvmField
		var armUp3 = 0.8;

		@JvmField
		var rotatorDown = 1.0;

		@JvmField
		var rotatorUp = 0.4;

		@JvmField
//		var intakeRotatorPos = 0.57;
		var intakeRotatorPos = 0.0;

		@JvmField
		var slidePosition = -1400;

		@JvmField
		var hslideScore = HSlide.score;

		@JvmField
		var intakeArmPos = 0.33;
	}

	private val colorSensorBad = ColorSensor.RED;

	override fun runOpMode()
	{
		val claw = hardwareMap.servo.get("outtakeClaw");
		val outtakeArm = hardwareMap.servo.get("outtakeArm");
		val clawRotator = hardwareMap.servo.get("clawRotator");

		val slide = Slide(hardwareMap);
		val arm = Arm(hardwareMap);
		val intake = Intake(hardwareMap);
		val specimenClaw = SpeciminClaw(hardwareMap);
		val specimenOuttake = SpecimenOuttake(specimenClaw, slide);

		val hSlide = HSlide(hardwareMap);

		val colorSensor = ColorSensor(hardwareMap, gamepad1, colorSensorBad);

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		drive.setPosEstimate(Pose2d(0.0, 0.0, rotPos));

		var localHeading = 0.0;
		var imuHeading = 0.0;

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		hSlide.zero();
		arm.up();
		specimenOuttake.init();

		claw.position = clawOpen;
		outtakeArm.position = armUp2;
		clawRotator.position = rotatorDown;
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

		var outtakeState = 0;

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
			if(gamepad1.right_trigger >= 0.5)
			{
				hSlide.increment();
			}
			else if(gamepad1.left_trigger >= 0.5)
			{
				hSlide.decrement();
			}
			else if(gamepad.triangle())
			{
				hSlide.gotoPos(hslideScore);
				arm.up();
				intake.zeroRotator();
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

			if(gamepad.ps())
			{
				slide.lowerTo(3000);
			}

			slide.update();

			// Intake Arm
			if(gamepad.square())
			{
				if(arm.state == Arm.State.Up)
				{
					intake.zeroRotator();
					delay(0.2);
					arm.down();
					if(colorSensor.col != ColorSensor.NONE)
					{
						intake.reverse();
						intake.stopIn(0.75);
					}
				}
				else if(arm.state == Arm.State.Down)
				{
					arm.up();
				}
			}

			if(gamepad.circle())
			{
				claw.position = clawOpen;
				outtakeArm.position = armDown;
				delay(0.2);
				claw.position = clawClose;
				intake.reverse();
				delay(0.2);
				outtakeArm.position = armUp3;
				intake.stop();
			}

			if(gamepad.cross())
			{
				when(outtakeState)
				{
					0 ->
					{
						clawRotator.position = rotatorUp;
						outtakeArm.position = armUp;
						slide.gotoPos(slidePosition);
						outtakeState = 1;
					}

					1 ->
					{
						claw.position = clawOpen;
						delay(0.2);
						clawRotator.position = rotatorDown;
						outtakeArm.position = armUp2;
						slide.lower();
						outtakeState = 0;
					}
				}
			}

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

			if(gamepad.dpadRight())
			{
				if(specimenClaw.state == SpeciminClaw.State.Open)
					specimenClaw.close();
				else
					specimenClaw.open();
			}
			specimenOuttake.update();

			colorSensor.update();
			telemetry.addData("imu heading", imuHeading);
			telemetry.addData("localizer heading", localHeading);
			slide.telem(telemetry);
			drive.telem(telemetry);
			colorSensor.telem(telemetry);
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
