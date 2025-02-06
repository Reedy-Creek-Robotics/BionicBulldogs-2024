package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class Stik (hardwareMap: HardwareMap) {

    private val stik = hardwareMap.servo.get("stick");

    enum class State {
        Raised,
        Lowered
    }

    var state: State = State.Raised;

    companion object
    {
        @JvmField
        var lower: Double = 0.1;
        @JvmField
        var raise: Double = 0.73;
    }

    init {
        stik.position = raise;
        state = State.Raised;
    }

    fun lower()
    {
        stik.position = lower;
        state = State.Lowered;
    }

    fun raise()
    {
        stik.position = raise;
        state = State.Raised;
    }
}