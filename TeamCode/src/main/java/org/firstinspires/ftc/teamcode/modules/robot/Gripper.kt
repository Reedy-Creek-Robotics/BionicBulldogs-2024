package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo

@Config
class Gripper(private val gripper: CRServo) {

    enum class State {
        Forward,
        Reverse,
        Stop
    }

    var state: State = State.Forward;

    companion object
    {
        @JvmField
        val gripPower = 1.0;
    }

    fun forward()
    {
        gripper.power -= gripPower;
        state = State.Reverse;
    }
    fun reverse()
    {
        gripper.power += gripPower;
        state = State.Forward;
    }
    fun stop()
    {
        gripper.power = 0.0;
        state = State.Stop;
    }

}