package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo

@Config
class Gripper(private val gripper: CRServo) {

    enum class State {
        Open,
        Close
    }

    var state: State = State.Open;

    companion object
    {
        @JvmField
        val gripPower = 1.0;
    }

    fun reverse()
    {
        gripper.power -= gripPower;
    }
    fun forward()
    {
        gripper.power += gripPower;
    }

}