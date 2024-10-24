package org.firstinspires.ftc.teamcode.opmode.functions

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.opmode.config.Claw
import org.firstinspires.ftc.teamcode.opmode.config.Slide

abstract class ClawF: LinearOpMode() {

    fun work(){
    val claw = Claw(hardwareMap.servo.get("claw"));
    val slide = Slide(hardwareMap.dcMotor.get("slide"));
    val gamepad = GamepadEx(gamepad1);

        fun runOpMode() {
            slide.reverse();

            waitForStart();

            claw.close();

            while (opModeIsActive()) {
                if(gamepad.circle())
                {
                    if(claw.state == Claw.State.Closed)
                    {
                        claw.open();
                    }
                    else if(claw.state == Claw.State.Open)
                    {
                        claw.close();
                    }
                }

                if(gamepad.cross())
                {
                    if(slide.state == Slide.State.Raise)
                    {
                        slide.lower();
                    }
                    else if(slide.state == Slide.State.Lower)
                    {
                        slide.raise();
                    }
                }
            }
        }
    }
}