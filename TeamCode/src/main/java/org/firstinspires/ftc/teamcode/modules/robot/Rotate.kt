package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo

@Config
class Rotate(private val rotateServo: CRServo) {

    companion object
    {
        @JvmField
        var rotatePower: Double = 0.5;
    }

    fun forward()
    {
        rotateServo.power = rotatePower;
    }

    fun reverse()
    {
        rotateServo.power = -rotatePower;
    }

    fun stop()
    {
        rotateServo.power = 0.0;
    }

}