package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo

@Config
class Gripper(private val gripper: Servo) {

    enum class State {
        Open,
        Close
    }

    var state: State = State.Open;

    companion object
    {
        @JvmField
        var gripClose : Double = 0.0;
        @JvmField
        var gripOpen : Double = 0.25;
    }

    fun close()
    {
        gripper.position = gripClose
        state = State.Close;
    }
    fun open()
    {
        gripper.position = gripOpen
        state = State.Open;
    }

}