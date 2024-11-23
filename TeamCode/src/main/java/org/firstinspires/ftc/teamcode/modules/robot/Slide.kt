package org.firstinspires.ftc.teamcode.modules.robot

import android.renderscript.RSInvalidStateException
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.abs

@Config
class Slide(private val slides: DcMotorEx)
{

	enum class State
	{
		Lower, Raise
	}

	var state = State.Lower;

	companion object
	{
		@JvmField
		var slideSpeed = 1.0;

		@JvmField
		var Top = 1800;

		@JvmField
		var Bottom = 0;
	}

	var stalled = false;
	var prevPos = 0;
	val elapsedTime = ElapsedTime();

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

	fun getPos(): Int
	{
		return slides.currentPosition;
	}

	fun raise()
	{
		runToPosition(-1400);
		state = State.Raise;
	}

	fun gotoPos(pos: Int)
	{
		runToPosition(pos);
		state = State.Raise;
	}

	fun lower()
	{
		runToPosition(0);
		state = State.Lower;
	}

	fun update()
	{
		/*if(state == State.Lower && abs(slides.currentPosition) < 250)
		{
			slides.power = 0.0;
		}*/
		if(state == State.Lower)
		{
			if(abs(prevPos - slides.currentPosition) < 10 && slides.power > 0.0)
			{
				if(!stalled)
				{
					elapsedTime.reset();
					stalled = true;
				}
				if(elapsedTime.seconds() > 0.5)
				{
					slides.power = 0.0;
					slides.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
					slides.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
					stalled = false;
				}
			}
			else
			{
				stalled = false;
			}
		}
		prevPos = slides.currentPosition;
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

	fun runToPosition(pos: Int, power: Double = -1.0)
	{
		slides.power = 0.0;
		slides.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slides.targetPosition = pos;
		slides.mode = DcMotor.RunMode.RUN_TO_POSITION;
		slides.power = power;
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
		t.addData("Slides: current", slides.getCurrent(CurrentUnit.AMPS));
	}
}