package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class StikClaw (hardwareMap: HardwareMap) {

    private val stik = hardwareMap.servo.get("sclaw");

    enum class State {
        Closed,
        Opened
    }

    var state: State = State.Closed;

    companion object
    {
        @JvmField
        var close: Double = 0.5;
        @JvmField
        var open: Double = 0.0;
    }

    init {
        stik.position = close;
        state = State.Closed;
    }

    fun open()
    {
        stik.position = open;
        state = State.Opened;
    }

    fun close()
    {
        stik.position = close;
        state = State.Closed;
    }
}