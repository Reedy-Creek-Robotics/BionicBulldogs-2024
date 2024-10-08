package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.Servo

class Arm (private val arm: Servo) {

    companion object
    {
        @JvmField
        var armClose : Double = 0.0;
        @JvmField
        var armOpen : Double = 0.63;
    }

    fun down()
    {
        arm.position = armClose;
    }

    fun up()
    {
        arm.position = armOpen;
    }
}