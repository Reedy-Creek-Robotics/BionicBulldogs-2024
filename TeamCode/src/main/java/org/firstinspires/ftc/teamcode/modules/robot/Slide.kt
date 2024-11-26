package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.abs

@Config
class Slide(hardwareMap: HardwareMap)
{
	private val slide = hardwareMap.dcMotor.get("slide") as DcMotorEx;

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

	private var stalled = false;
	private var prevPos = 0;
	private val elapsedTime = ElapsedTime();

	init
	{
		slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
		slide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
	}

	fun reverse()
	{
		slide.direction = DcMotorSimple.Direction.REVERSE;
	}

	fun getPos(): Int
	{
		return slide.currentPosition;
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
			if(abs(prevPos - slide.currentPosition) < 10 && slide.power > 0.0)
			{
				if(!stalled)
				{
					elapsedTime.reset();
					stalled = true;
				}
				if(elapsedTime.seconds() > 0.5)
				{
					slide.power = 0.0;
					slide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
					slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
					stalled = false;
				}
			}
			else
			{
				stalled = false;
			}
		}
		prevPos = slide.currentPosition;
	}

	fun up()
	{
		slide.power = slideSpeed;
	}

	fun down()
	{
		slide.power = -slideSpeed;
	}

	fun stop()
	{
		slide.power = 0.0;
	}

	fun runToPosition(pos: Int, power: Double = -1.0)
	{
		slide.power = 0.0;
		slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slide.targetPosition = pos;
		slide.mode = DcMotor.RunMode.RUN_TO_POSITION;
		slide.power = power;
	}

	fun busy(): Boolean
	{
		return slide.isBusy;
	}

	fun telem(t: Telemetry)
	{
		t.addData("Slides: position", slide.currentPosition);
		t.addData("Slides: targetPosition", slide.targetPosition);
		t.addData("Slides: power", slide.power);
		t.addData("Slides: current", slide.getCurrent(CurrentUnit.AMPS));
	}
}