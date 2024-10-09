package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.Servo

class Arm (private val arm: Servo) {

    enum class State {
        Down,
        Up
    }

    var state: State = State.Up

    companion object
    {
        @JvmField
        var armDown : Double = 0.98;
        @JvmField
        var armUp : Double = 0.5;
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