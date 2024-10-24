package org.firstinspires.ftc.teamcode.opmode.config

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo

@Config
class Rotate(private val rotateServo: CRServo) {

    enum class State {
        Forward,
        Reverse,
        Stop
    }
    var state = State.Stop;

    companion object
    {
        @JvmField
        var rotatePower: Double = 0.5;
    }

    fun forward()
    {
        rotateServo.power = rotatePower;
        state = State.Forward;
    }

    fun reverse()
    {
        rotateServo.power = -rotatePower;
        state = State.Reverse;
    }

    fun stop()
    {
        rotateServo.power = 0.0;
        state = State.Stop;
    }

}