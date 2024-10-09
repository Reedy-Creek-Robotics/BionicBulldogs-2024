package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.CRServo

class Spin(private val spin0: CRServo, private val spin1: CRServo) {

    //spin0 = rotator0 = frontrot = port2
    //spin1 = rotator1 = backrot = port3

    enum class State
    {
        Forward,
        Stop,
        Reverse
    }

    var state: State = State.Stop;

    companion object
    {
        @JvmField
        var spinPower: Double = 0.5;
        @JvmField
        var spinStop = 0.0;
    }

    fun forward()
    {
        spin0.power = -spinPower;
        spin1.power = -spinPower;
        state = State.Forward;
    }

    fun reverse()
    {
        spin0.power = spinPower;
        spin1.power = spinPower;
        state = State.Reverse;
    }

    fun stop()
    {
        spin0.power = spinStop;
        spin1.power = spinStop;
        state = State.Stop;
    }
}