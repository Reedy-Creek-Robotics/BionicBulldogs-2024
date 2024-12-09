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
	private val slide2 = hardwareMap.dcMotor.get("slide2") as DcMotorEx;

  enum class Mode
  {
    Manual, Automatic
  }

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

		@JvmField
		var relesePos = -1000;

		@JvmField
		var specimenPos = 1350;

		@JvmField
		var stallDifference = 1;
	}

	private var stalled = false;
	private var prevPos = 0;
  private var mode = Mode.Automatic;
	private val elapsedTime = ElapsedTime();

	init
	{
		slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
		slide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
		slide2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slide2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
		slide2.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
		slide2.direction = DcMotorSimple.Direction.REVERSE;
	}

	fun reverse()
	{
		slide.direction = DcMotorSimple.Direction.REVERSE;
		slide2.direction = DcMotorSimple.Direction.FORWARD;
	}

	fun getPos(): Int
	{
		return slide.currentPosition;
	}

	fun raise()
	{
		runToPosition(-specimenPos);
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

	fun specimenLower()
	{
		runToPosition(relesePos);
	}

	fun update()
	{
		/*if(state == State.Lower && abs(slides.currentPosition) < 250)
		{
			slides.power = 0.0;
		}*/
		if(state == State.Lower && slide.mode == DcMotor.RunMode.RUN_TO_POSITION)
		{
			if(abs(prevPos - slide.currentPosition) < stallDifference && slide.power > 0.0)
			{
				if(!stalled)
				{
					elapsedTime.reset();
					stalled = true;
				}
				if(elapsedTime.seconds() > 0.5)
				{
					slide.power = 0.0;
					slide2.power = 0.0;
					slide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
					slide2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
					slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
					slide2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
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

  fun manualMode()
  {
    if(mode == Mode.Manual)
    {
      slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
      slide2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
      mode = Mode.Manual;
      slide.power = 0.0;
      slide2.power = 0.0;
    }
  }

	fun up()
	{
    manualMode();
		slide.power = slideSpeed;
		slide2.power = slideSpeed;
	}

	fun down()
	{
    manualMode();
		slide.power = -slideSpeed;
		slide2.power = -slideSpeed;
	}

	fun stop()
	{
    if(mode == Mode.Manual)
    {
		  slide.power = 0.0;
		  slide2.power = 0.0;
    }
	}

	fun runToPosition(pos: Int, power: Double = -1.0)
	{
    mode = Mode.Automatic;
		prevPos = 400000;
		slide.power = 0.0;
		slide2.power = 0.0;
		slide.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slide2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
		slide.targetPosition = pos;
		slide2.targetPosition = pos;
		slide.mode = DcMotor.RunMode.RUN_TO_POSITION;
		slide2.mode = DcMotor.RunMode.RUN_TO_POSITION;
		slide.power = if(power == -1.0) slideSpeed else power;
		slide2.power = if(power== -1.0) slideSpeed else power;
	}

	fun busy(): Boolean
	{
		return slide.isBusy;
	}

	fun telem(t: Telemetry)
	{
    t.addData("Slides: Mode", mode);

		t.addData("Slide: position", slide.currentPosition);
		t.addData("Slide: targetPosition", slide.targetPosition);
		t.addData("Slide: power", slide.power);
		t.addData("Slide: current", slide.getCurrent(CurrentUnit.AMPS));

		t.addData("Slide2: position", slide2.currentPosition);
		t.addData("Slide2: targetPosition", slide2.targetPosition);
		t.addData("Slide2: power", slide2.power);
		t.addData("Slide2: current", slide2.getCurrent(CurrentUnit.AMPS));
	}
}
