package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx
import org.firstinspires.ftc.teamcode.modules.robot.Stik

//Stik should just be bucketarm in bot_v2
@TeleOp
class StikTest: LinearOpMode() {
    override fun runOpMode() {

        val stik = Stik(hardwareMap);
        val gamepad = GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            gamepad.copy();

            if (gamepad.square()) {
                if (stik.state == Stik.State.Lowered) {
                    stik.raise();
                } else if (stik.state == Stik.State.Raised) {
                    stik.lower();
                }
            }
        }
    }
}