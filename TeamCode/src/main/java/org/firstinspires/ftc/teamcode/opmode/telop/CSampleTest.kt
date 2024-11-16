package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.Gripper
import org.firstinspires.ftc.teamcode.modules.robot.Spin
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class CSampleTest: LinearOpMode() {
    override fun runOpMode() {

        val arm = Arm(hardwareMap.servo.get("arm"));
        val gripper = Gripper(hardwareMap.crservo.get("gripper"));
        val spin = Spin(hardwareMap.crservo.get("rotator0"), hardwareMap.crservo.get("rotator1"));
        val drive = HDrive(HDriveConfig(hardwareMap));
        drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")));

        val gamepad = GamepadEx(gamepad1);

        waitForStart();

        arm.down()

        while (opModeIsActive()) {
            gamepad.copy()

            drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if(gamepad.leftTriggerb())
            {
                arm.up()
            };

            if(gamepad.rightTriggerb())
            {
                arm.down()
            };

            if(gamepad.leftBumper())
            {
                if (gripper.state != Gripper.State.Forward)
                {
                    gripper.forward();
                } else {
                    gripper.reverse();
                }
            };

            if(gamepad.rightBumper())
            {
                if (spin.state != Spin.State.Forward)
                {
                    spin.forward();
                } else {
                    spin.reverse();
                }
            };

            if(gamepad.dpadLeft() || gamepad.dpadRight())
            {
                spin.stop()
            }
        }
    }
}