package org.firstinspires.ftc.teamcode.opmode.config

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.Telemetry

@Config
class Slide(private val slides: DcMotor)
{

	enum class State
	{
		Lower, Raise
	}

	var state = State.Raise;

	companion object
	{
		@JvmField
		var slideSpeed = 0.5;

		@JvmField
		var Top = 1800;

		@JvmField
		var Bottom = 0;
	}

	init
	{
		slides.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slides.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
		slides.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
	}

	fun reverse()
	{
		slides.direction = DcMotorSimple.Direction.REVERSE;
	}

	fun raise()
	{
		runToPosition(1400);
		state = State.Lower;
	}

	fun lower()
	{
		runToPosition(0);
		state = State.Raise;
	}

	fun up()
	{
		slides.power = slideSpeed;
	}

	fun down()
	{
		slides.power = -slideSpeed;
	}

	fun stop()
	{
		slides.power = 0.0;
	}

	fun runToPosition(pos: Int)
	{
		slides.power = 0.0;
		slides.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slides.targetPosition = pos;
		slides.mode = DcMotor.RunMode.RUN_TO_POSITION;
		slides.power = slideSpeed;
	}

	fun busy(): Boolean
	{
		return slides.isBusy;
	}

	fun telem(t: Telemetry)
	{
		t.addData("Slides: position", slides.currentPosition);
		t.addData("Slides: targetPosition", slides.targetPosition);
		t.addData("Slides: power", slides.power);
	}
}