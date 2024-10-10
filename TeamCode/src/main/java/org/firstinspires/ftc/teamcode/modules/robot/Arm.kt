package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class Arm (private val arm: Servo) {

    companion object
    {
        @JvmField
        var armDown : Double = 0.5;
        @JvmField
        var armUp : Double = 0.96;
    }

    fun down()
    {
        arm.position = armDown;
    }

    fun up()
    {
        arm.position = armUp;
    }
}