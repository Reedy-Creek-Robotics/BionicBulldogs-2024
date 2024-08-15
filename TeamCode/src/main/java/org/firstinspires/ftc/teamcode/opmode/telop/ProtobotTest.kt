package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
class ProtobotTest: OpMode()
{
		private lateinit var leftMotor: DcMotor;
		private lateinit var rightMotor: DcMotor;
		override fun init()
		{
				leftMotor = hardwareMap.dcMotor.get("left");
				rightMotor = hardwareMap.dcMotor.get("right");
				rightMotor.direction = DcMotorSimple.Direction.REVERSE;
		}

		override fun loop()
		{
				val forward = gamepad1.left_stick_y;
				val turn = gamepad1.right_stick_x;
				leftMotor.power = (forward + turn).toDouble();
				rightMotor.power = (forward - turn).toDouble();
		}
}