package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class SampleClaw(hardwareMap: HardwareMap)
{
	companion object
	{
		@JvmField
		var clawOpen = 0.6;

		@JvmField
		var clawClose = 0.97;

		@JvmField
		var armGrab = 0.89;

		@JvmField
		var armUp = 0.2;

		@JvmField
		var armStart = 0.75;

		@JvmField
		var rotatorDown = 0.2;

		@JvmField
		var rotatorUp = 0.7;
	}

	private val claw: Servo = hardwareMap.servo.get("outtakeClaw");
	private val arm: Servo = hardwareMap.servo.get("outtakeArm");
	private val rotator: Servo = hardwareMap.servo.get("clawRotator");

	fun init()
	{
		claw.position = clawClose;
		arm.position = armStart;
		rotator.position = rotatorDown;
	}

	fun open()
	{
		claw.position = clawOpen;
	}

	fun close()
	{
		claw.position = clawClose;
	}

	fun armUp()
	{
		arm.position = armUp;
	}

	fun armGrab()
	{
		arm.position = armGrab;
	}

	fun armStart()
	{
		arm.position = armStart;
	}

	fun rotatorDown()
	{
		rotator.position = rotatorDown;
	}

	fun rotatorUp()
	{
		rotator.position = rotatorUp;
	}
}