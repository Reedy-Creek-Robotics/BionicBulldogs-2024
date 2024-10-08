package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.Servo

class Gripper(private val gripper: Servo) {

    companion object
    {
        @JvmField
        var gripClose : Double = 0.0
        @JvmField
        var gripOpen : Double = 0.25
    }

    fun close()
    {
        gripper.position = gripClose
    }
    fun open()
    {
        gripper.position = gripOpen
    }

}