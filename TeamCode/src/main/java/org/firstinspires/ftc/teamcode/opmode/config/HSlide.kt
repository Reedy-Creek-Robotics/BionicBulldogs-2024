package org.firstinspires.ftc.teamcode.opmode.config

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class HSlide(private val hSlide: Servo)
{
    companion object
    {
        @JvmField
        var increment = 0.01;
    }

    init
    {
        hSlide.position = 0.0;
    }

    /*fun reverse()
    {
        hSlide.direction = DcMotorSimple.Direction.REVERSE;
    }*/

    fun increment()
    {
        hSlide.position += increment;
    }

    fun decrement()
    {
        hSlide.position -= increment;
    }

    fun zero()
    {
        hSlide.position = 0.0;
    }
}