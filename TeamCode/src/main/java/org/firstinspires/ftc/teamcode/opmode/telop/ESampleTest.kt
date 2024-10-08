package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.Rotate
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class ESampleTest: LinearOpMode()
{
    override fun runOpMode()
    {
        val rotate = Rotate(hardwareMap.crservo.get("rotate"));
        val arm = Arm(hardwareMap.servo.get("arm"));
        val drive = HDrive(HDriveConfig(hardwareMap));
        drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")))

        val gamepad = GamepadEx(gamepad1);

        waitForStart();

        arm.down()

        while(opModeIsActive())
        {
            gamepad.copy()

            drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if(gamepad.leftBumper())
            {
                arm.down()
            };

            if(gamepad.rightBumper())
            {
                arm.up()
            };

            if(gamepad.square())
            {
                rotate.reverse()
            };

            if(gamepad.triangle())
            {
                rotate.forward()
            };

            if (gamepad.cross())
            {
                rotate.stop()
            }
        }
    }
}