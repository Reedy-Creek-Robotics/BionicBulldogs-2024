package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.drive.SparkfunImuLocalizer
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

@TeleOp
class ESampleTest: LinearOpMode()
{
    override fun runOpMode()
    {
        val rotate = Intake(hardwareMap);
        val arm = Arm(hardwareMap);
        val drive = HDrive(HDriveConfig(hardwareMap));
        drive.setLocalizer(SparkfunImuLocalizer(hardwareMap.get(SparkFunOTOS::class.java, "imu2")))
        var square = 0;
        var rBump = 0;
        var lBump = 0;

        waitForStart();

        val gamepad = GamepadEx(gamepad1);

        while(opModeIsActive())
        {
            gamepad.copy()

            drive.driveFR(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if(gamepad.square())
            {
                square++
                if(arm.state == Arm.State.Up) {
                    arm.down()
                } else if(arm.state == Arm.State.Down) {
                    arm.up()
                }
            }

            if(gamepad.rightBumper())
            {
                rBump++
                if (rotate.state != Intake.State.Reverse)
                {
                    rotate.reverse()
                } else {
                    rotate.stop()
                }
            }

            if(gamepad.leftBumper())
            {
                lBump++
                if (rotate.state != Intake.State.Forward)
                {
                    rotate.forward()
                } else {
                    rotate.stop()
                }
            }

            telemetry.addData("rState: ", rotate.state);
            telemetry.addData("aState: ", arm.state);
            telemetry.addData("lBumper", gamepad.leftBumper());
            telemetry.addData("rBumper", gamepad.rightBumper());
            telemetry.addData("square", square);
            telemetry.addData("rCount", rBump);
            telemetry.addData("lCount", lBump);
            telemetry.update();

        }
    }
}
