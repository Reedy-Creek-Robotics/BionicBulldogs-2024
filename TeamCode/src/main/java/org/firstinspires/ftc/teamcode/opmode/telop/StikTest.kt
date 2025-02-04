package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Stik
import org.firstinspires.ftc.teamcode.modules.robot.StikClaw
import org.firstinspires.ftc.teamcode.modules.robot.StikRotate

//Stik should just be bucketarm in bot_v2
@TeleOp
class StikTest: LinearOpMode() {
    override fun runOpMode() {

        val stik = Stik(hardwareMap);
        val stikClaw = StikClaw(hardwareMap);
        val stikRotate = StikRotate(hardwareMap);
        val gamepad = GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            gamepad.copy();

            if (gamepad.square()) {
                if (stik.state == Stik.State.Raised) {
                    stik.raise();
                    stikRotate.outake();
                } else if (stik.state == Stik.State.Lowered) {
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
        }
    }
}