package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.IMULocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.ImuEx
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp(group = "test")
class FRHDriveTest: LinearOpMode()
{
	//Sample intake
	private lateinit var arm: Servo
	private lateinit var rotateServo: CRServo

	//Drivetrain
	private lateinit var hDrive: HDrive

	//Specimen claw
	private lateinit var claw: Servo
	private lateinit var slide: DcMotor

	//Caleb's mechanism
	private lateinit var gripper: Servo
	private lateinit var rotator0: CRServo
	private lateinit var rotator1: CRServo

	override fun runOpMode()
	{
		hDrive = HDrive(HDriveConfig(hardwareMap))

		arm = hardwareMap.servo.get("arm")
		//rotateServo = hardwareMap.crservo.get("rotate")
		claw = hardwareMap.servo.get("claw")
		slide = hardwareMap.dcMotor.get("slide")
		gripper = hardwareMap.servo.get("gripper")
		rotator0 = hardwareMap.crservo.get("rotator0")
		rotator1 = hardwareMap.crservo.get("rotator1")

		slide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		slide.direction = DcMotorSimple.Direction.REVERSE

		val imu = ImuEx(hardwareMap.get(IMU::class.java, "imu"))

		hDrive.setLocalizer(IMULocalizer(imu))

		waitForStart()

		slide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
		slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
		/*slide.targetPosition = 0
		slide.mode = DcMotor.RunMode.RUN_TO_POSITION*/

		while(opModeIsActive())
		{
			val forward = gamepad1.left_stick_y
			val right = gamepad1.left_stick_x
			val rotate = gamepad1.right_stick_x

			hDrive.driveFR(forward, right, rotate)
			hDrive.telem(telemetry)

			cMech()

			telemetry.addData("forward power:", forward)
			telemetry.addData("strafe power:", right)
			telemetry.addData("rotate power:", rotate)
			telemetry.addData("arm pos: ", arm.position)

			telemetry.update()

		}
	}

	//Costs bumpers and all buttons except Y
	private fun eMech() {

		//Eli's mechanism

		if (gamepad1.left_bumper) {
			arm.position = 0.0
		}; if (gamepad1.right_bumper) {
			arm.position = 0.63
		}; if (gamepad1.x) {
			rotateServo.power = -0.5
			//changed from gamepad.b -> gamepad.y
		}; if (gamepad1.y) {
			rotateServo.power = 0.5
		}; if (gamepad1.a) {
			rotateServo.power = 0.0
		};

	}

	//Costs horizontal dpad and triggers
	private fun jMech() {

		//JD's mechanism

		if (gamepad1.dpad_left) {
			claw.position = 0.0
		}; if (gamepad1.dpad_right) {
			claw.position = 1.0
		};

		/* RUN_TO_POSITION

		if (gamepad1.right_trigger >= 0.5) {
			slide.targetPosition = 1800
			slide.power = 0.25
		}; if (gamepad1.left_trigger >= 0.5) {
			slide.targetPosition = 0
			slide.power = 0.25
		}; slide.targetPosition = (slide.targetPosition).absoluteValue*/

		//No target position
		if (gamepad1.right_trigger >= 0.25) {
			slide.power = (gamepad1.right_trigger).toDouble()
		} else if (gamepad1.left_trigger >= 0.25) {
			slide.power = -(gamepad1.left_trigger).toDouble()
		} else {
			slide.power = 0.0
		}

		telemetry.addData("slide pos:", slide.currentPosition)
		telemetry.addData("slide target:", slide.targetPosition)

	}

	//Costs dpad and horizontal buttons (x/b)
	private fun cMech() {

		//Caleb's mechanism

		if (gamepad1.left_bumper) {
			arm.position = 0.95
		}; if (gamepad1.right_bumper) {
			arm.position = 0.5
		}; if (gamepad1.x) {
			gripper.position = 0.01
		}; if (gamepad1.b) {
			gripper.position = 0.25
		}; if (gamepad1.dpad_up) {
			rotator0.power = 0.5
			rotator1.power = -0.5
		}; if (gamepad1.dpad_down) {
			rotator0.power = -0.5
			rotator1.power = 0.5
		}; if (gamepad1.dpad_left || gamepad1.dpad_right) {
			rotator0.power = 0.0
			rotator1.power = 0.0
		}

		telemetry.addData("grip pos: ", gripper.position)

	}
}