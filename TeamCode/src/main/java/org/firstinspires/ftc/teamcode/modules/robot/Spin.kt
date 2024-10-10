package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo

@Config
class Spin(private val spin0: CRServo, private val spin1: CRServo) {

    companion object
    {
        @JvmField
        var spinPower: Double = 0.5;
        @JvmField
        var spinStop = 0.0;
    }

    fun forward()
    {
        spin0.power = spinPower;
        spin1.power = -spinPower;
    }

    fun reverse()
    {
        spin0.power = -spinPower;
        spin1.power = spinPower;
    }

    fun stop()
    {
        spin0.power = spinStop;
        spin1.power = spinStop;
    }
}