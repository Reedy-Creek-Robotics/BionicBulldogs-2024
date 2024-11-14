package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.opmode.config.Slide
import org.firstinspires.ftc.teamcode.opmode.config.Spin
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class EmSampleTest: LinearOpMode() {
    override fun runOpMode() {

        val spin = Spin(hardwareMap.crservo.get("claw"), hardwareMap.crservo.get("gripper"));
        val slide = Slide(hardwareMap.dcMotor.get("slide"));
        val drive = HDrive(HDriveConfig(hardwareMap));
        drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

        waitForStart();

        val gamepad = GamepadEx(gamepad1);
        slide.reverse();

        while(opModeIsActive())
        {
            gamepad.copy();

            drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if(gamepad.rightBumper())
            {
                if (spin.state != Spin.State.Forward)
                {
                    spin.forward();
                } else if (spin.state != Spin.State.Reverse)
                {
                    spin.reverse();
                }
            }
            if(gamepad.leftBumper())
            {
                spin.stop();
            }
            if(gamepad1.dpad_up)
            {
                slide.up();
            }

            if(gamepad1.dpad_down)
            {
                slide.down();
            }

            if(!gamepad1.dpad_up && !gamepad1.dpad_down)
            {
                slide.stop();
            }
        }
    }
}