package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class StikRotate(hardwareMap: HardwareMap) {

    private val stik = hardwareMap.servo.get("srotate");

    enum class State {
        Outake,
        Intake
    }

    var state: State = State.Outake;

    companion object
    {
        @JvmField
        var intake: Double = 0.9;
        @JvmField
        var outake: Double = 0.0;
    }

    init {
        stik.position = outake;
        state = State.Outake;
    }

    fun intake()
    {
        stik.position = intake;
        state = State.Intake;
    }

    fun outake()
    {
        stik.position = outake;
        state = State.Outake;
    }
}