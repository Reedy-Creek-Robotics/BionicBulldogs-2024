package org.firstinspires.ftc.teamcode.opmode.telop

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
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
		var armDown = 0.87;

		@JvmField
		var armUp = 0.2;

		@JvmField
		var armUp2 = 0.75;

		@JvmField
		var armUp3 = 0.7;

		@JvmField
		var rotatorDown = 0.2;

		@JvmField
		var rotatorGrab = 0.2;

		@JvmField
		var rotatorUp = 0.7;

		@JvmField
//		var intakeRotatorPos = 0.57;
		var intakeRotatorPos = 0.0;

		@JvmField
		var intakeArmCollect = 0.75;

		@JvmField
		var slidePosition = -1600;

		@JvmField
		var hslideScore = HSlide.score;

		@JvmField
		var hslideGrab = HSlide.score + 0.01;

		@JvmField
		var intakeArmPos = 0.33;

		@JvmField
		var transferDelay1 = 0.3;
		@JvmField
		var transferDelay2 = 0.2;
		@JvmField
		var transferDelay3 = 0.1;
		@JvmField
		var transferDelay4 = 0.2;
		@JvmField
		var transferDelay5 = 0.2;
	}

	private var grabState = 0;
	private var grabDelay = 0.0;
	private val grabElapsedTime = ElapsedTime();

	private val colorSensorBad = ColorSensor.RED;

	private lateinit var arm: Arm;
	private lateinit var intake: Intake;
	private lateinit var slide: Slide;
	private lateinit var hslide: HSlide;
	private lateinit var claw: Servo;
	private lateinit var outtakeArm: Servo;
	private lateinit var clawRotator: Servo;

	override fun runOpMode()
	{
		claw = hardwareMap.servo.get("outtakeClaw");
		outtakeArm = hardwareMap.servo.get("outtakeArm");
		clawRotator = hardwareMap.servo.get("clawRotator");

		slide = Slide(hardwareMap);
		arm = Arm(hardwareMap);
		intake = Intake(hardwareMap);
		val specimenClaw = SpeciminClaw(hardwareMap);
		val specimenOuttake = SpecimenOuttake(specimenClaw, slide);

		hslide = HSlide(hardwareMap);

		val colorSensor = ColorSensor(hardwareMap, gamepad1, colorSensorBad);

		val drive = HDrive(HDriveConfig(hardwareMap));
		drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

		drive.setPosEstimate(Pose2d(0.0, 0.0, rotPos));

		var localHeading = 0.0;
		var imuHeading = 0.0;

		val gamepad = GamepadEx(gamepad1);

		waitForStart();

		hslide.zero();
		arm.up();
		specimenOuttake.init();

		claw.position = clawClose;
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
				hslide.increment();
			}
			else if(gamepad1.left_trigger >= 0.5)
			{
				hslide.decrement();
			}
			else if(gamepad.triangle())
			{
				hslide.gotoPos(hslideScore);
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
					//delay(0.2);
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
				grabState = 1;
			}

			if(gamepad.cross())
			{
				when(outtakeState)
				{
					0 ->
					{
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
			if(slide.getPos() < -500 && outtakeState == 1)
			{
				clawRotator.position = rotatorUp;
				outtakeArm.position = armUp;
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

			updateGrab();

			specimenOuttake.update();

			colorSensor.update();
			telemetry.addData("imu heading", imuHeading);
			telemetry.addData("localizer heading", localHeading);
			slide.telem(telemetry);
			drive.telem(telemetry);
			colorSensor.telem(telemetry);
			specimenOuttake.telem(telemetry);
			telemetry.addData("hPos", hslide.pos())
			telemetry.addData("touchpad", gamepad1.touchpad);
			telemetry.update();
		}
	}

	private fun updateGrab()
	{
		when(grabState)
		{
			1 ->
			{
				hslide.gotoPos(hslideGrab);
				arm.gotoPos(intakeArmCollect);
				grabElapsedTime.reset();
				grabState = 2;
			}

			2 ->
			{
				if(grabElapsedTime.seconds() >= transferDelay1)
				{
					claw.position = clawOpen;
					outtakeArm.position = armDown;
					clawRotator.position = rotatorGrab;
					grabElapsedTime.reset();
					grabState = 3;
				}
			}

			3 ->
			{
				if(grabElapsedTime.seconds() >= transferDelay2)
				{
					claw.position = clawClose;
					grabElapsedTime.reset();
					grabState = 4;
				}
			}

			4 ->
			{
				if(grabElapsedTime.seconds() >= transferDelay3)
				{
					intake.reverse(1.0);
					grabElapsedTime.reset();
					grabState = 5;
				}
			}

			5 ->
			{
				if(grabElapsedTime.seconds() >= transferDelay4)
				{
					outtakeArm.position = armUp3;
					grabElapsedTime.reset();
					grabState = 6;
				}
			}

			6 ->
			{
				if(grabElapsedTime.seconds() >= transferDelay5)
				{
					intake.stop();
					grabState = 0;
				}
			}
		}
	}

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}
