package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.util.ElapsedTime

@Config
class Rotate(private val rotate0: CRServo) {

    enum class State {
        Forward,
        Reverse,
        Stop
    }
    var state = State.Stop;
    var targetTime = 0.0;
    val elapsedTime = ElapsedTime();

    companion object
    {
        @JvmField
        var rotatePower: Double = 0.5;
    }

    fun forward()
    {
        rotate0.power = rotatePower;
        state = State.Forward;
    }

    fun reverse()
    {
        rotate0.power = -rotatePower;
        state = State.Reverse;
    }

    fun stop()
    {
        rotate0.power = 0.0;
        state = State.Stop;
    }

    /**
     * @param time seconds
     */
    fun outtakeForTime(time: Double)
    {
        elapsedTime.reset();
        targetTime = time;
        reverse();
    }

    fun update()
    {
        if(targetTime > 0)
        {
            if(targetTime < elapsedTime.seconds())
            {
                forward();
                targetTime = -1.0;
            }
        }
    }
}