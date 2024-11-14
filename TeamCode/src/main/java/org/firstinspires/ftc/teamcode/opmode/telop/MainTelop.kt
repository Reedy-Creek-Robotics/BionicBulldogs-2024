package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.Claw
import org.firstinspires.ftc.teamcode.modules.robot.Rotate
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

class MainTelop: LinearOpMode() {
    override fun runOpMode() {

        //Claw, slides (to pos), drive, eTake
        val claw = Claw(hardwareMap.servo.get("claw"));
        val slide = Slide(hardwareMap.dcMotor.get("slide"));
        val arm = Arm(hardwareMap.servo.get("arm"));
        val rotate = Rotate(hardwareMap.crservo.get("rotate"))

        val drive = HDrive(HDriveConfig(hardwareMap));
        drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

        slide.reverse();

        val gamepad = GamepadEx(gamepad1);

        waitForStart();

        claw.close();
        arm.up();


        //Controls:
        //Claw - Circle toggles open/close
        //eTake (rotate) - rBumper toggles intake (forward), lBumper toggles intake (reverse)
        //Slide - Set to position, cross for toggling
        //Combo - Touchpad = Closes claw + raises slides
        //Arm - Square toggles up/down

        while(opModeIsActive())
        {
            gamepad.copy();

            //Drive
            drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            //Claw
            if (gamepad.circle()) {
                if (claw.state != Claw.State.Closed) {
                    claw.close();
                } else if (claw.state != Claw.State.Open) {
                    claw.open();
                }
            }

            //Rotate rBumper
            if (gamepad.rightBumper()) {
                if (rotate.state != Rotate.State.Stop) {
                    rotate.stop();
                } else if (rotate.state != Rotate.State.Forward) {
                    rotate.forward();
                }
            }

            //Rotate lBumper
            if(gamepad.leftBumper()) {
                if (rotate.state != Rotate.State.Stop) {
                    rotate.stop();
                } else if (rotate.state != Rotate.State.Reverse) {
                    rotate.reverse();
                }
            }

            //Slide
            //WIP -- Add in slide values
            if(gamepad.cross()) {
                if (slide.state == Slide.State.Raise) {
                    slide.raise();
                } else if (slide.state == Slide.State.Lower) {
                    slide.lower();
                }
            }

            //Combo
            if(gamepad.touchpad()) {
                claw.close();
                slide.raise();
            }

            //Arm
            if(gamepad.square()) {
                if(arm.state == Arm.State.Up) {
                    arm.down();
                } else if(arm.state == Arm.State.Down) {
                    arm.up();
                }
            }
        }
    }
}