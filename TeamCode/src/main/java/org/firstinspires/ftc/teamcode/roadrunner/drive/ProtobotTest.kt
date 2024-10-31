package org.firstinspires.ftc.teamcode.roadrunner.drive

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
@Config
class ProtobotTest: OpMode()
{
	companion object
	{
		@JvmField
		var speed = 0.6;
		@JvmField
		var turnSpeed = 0.3;
	}
	private lateinit var leftMotor: DcMotor;
	private lateinit var rightMotor: DcMotor;
	override fun init()
	{
		leftMotor = hardwareMap.dcMotor.get("left");
		rightMotor = hardwareMap.dcMotor.get("right");
		rightMotor.direction = DcMotorSimple.Direction.REVERSE;
		leftMotor.direction = DcMotorSimple.Direction.REVERSE;
	}

	override fun loop()
	{
		val forward = gamepad1.left_stick_y * speed;
		val turn = gamepad1.right_stick_x * turnSpeed;
		leftMotor.power = (forward - turn);
		rightMotor.power = (forward + turn);
	}
}