package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.drive.HDrive
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.Stik
import org.firstinspires.ftc.teamcode.modules.robot.StikClaw
import org.firstinspires.ftc.teamcode.modules.robot.StikRotate
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig

//Stik should just be bucketarm in bot_v2
@TeleOp
class StikTest: LinearOpMode() {
    override fun runOpMode() {

        val stik = Stik(hardwareMap);
        val stikClaw = StikClaw(hardwareMap);
        val stikRotate = StikRotate(hardwareMap);
        val gamepad = GamepadEx(gamepad1);
        val drive = HDrive(HDriveConfig(hardwareMap));
        val slide = Slide(hardwareMap);
        var slidepos = 0.0;

        //drive.setLocalizer(hardwareMap.get("imu"));

        waitForStart();

        while (opModeIsActive()) {
            drive.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x)
            gamepad.copy();

            if (gamepad.square()) {
                if (stik.state == Stik.State.Lowered) {
                    stikClaw.close();
                    sleep(500);
                    stik.raise();
                    stikRotate.outake();
                } else if (stik.state == Stik.State.Raised) {
                    stikClaw.open();
                    stik.lower();
                    stikRotate.intake();
                }
            }

            if (gamepad.circle()) {
                if (stikClaw.state == StikClaw.State.Closed) {
                    stikClaw.open();
                } else if (stikClaw.state == StikClaw.State.Opened) {
                    stikClaw.close();
                }
            }

            if (gamepad.leftTriggerb()) {
                slidepos += gamepad.leftTriggerf()
            } else if (gamepad.rightTriggerb()) {
                slidepos -= gamepad.rightTriggerf()
            }
            if (slide.getPos() < 0) {
                slidepos = 0.0
            }

            //slide.gotoPos(slidepos.toInt())

            telemetry.addData("Slide pos", slide.getPos())
            telemetry.addData("slide pos", slidepos)
            telemetry.update()
        }
    }
}