package org.firstinspires.ftc.teamcode.opmode.config

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class Arm (private val arm: Servo) {

    enum class State {
        Down,
        Up
    }

    var state: State = State.Up

    companion object
    {
        @JvmField
        var armUp : Double = 1.0;
        @JvmField
        var armDown : Double = 0.5;
    }

    fun down()
    {
        arm.position = armDown;
        state = State.Down;
    }

    fun up()
    {
        arm.position = armUp;
        state = State.Up;
    }
}