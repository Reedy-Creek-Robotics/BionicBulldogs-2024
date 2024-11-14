package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Config
class Slide(private val slides: DcMotor)
{

    enum class State {
        Lower,
        Raise
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
        /*slides.mode = DcMotor.RunMOde.STOP_AND_RESET_ENCODER;
        slides.position = Bottom;
        slides.mode = DcMotor.RunMode.RUN_TO_POSITION;*/
        slides.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
	}

	fun reverse()
	{
		slides.direction = DcMotorSimple.Direction.REVERSE;
	}

    fun raise()
    {
        //slides.setPosition(Top);
        state = State.Lower;
    }

    fun lower()
    {
        //slides.setPosition(Bottom);
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
}