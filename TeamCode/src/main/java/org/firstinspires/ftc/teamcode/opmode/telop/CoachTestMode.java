package org.firstinspires.ftc.teamcode.opmode.telop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.modules.robot.Claw;

public class CoachTestMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // By setting these values to new Gamepad(), they will default to all
        // boolean values as false and all float values as 0
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        // other initialization code goes here
        Claw claw = new Claw(hardwareMap.servo.get("claw"));

        waitForStart();

        while (opModeIsActive()) {
            // Store the gamepad values from the previous loop iteration in
            // previousGamepad1/2 to be used in this loop iteration.
            // This is equivalent to doing this at the end of the previous
            // loop iteration, as it will run in the same order except for
            // the first/last iteration of the loop.
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            // Store the gamepad values from this loop iteration in
            // currentGamepad1/2 to be used for the entirety of this loop iteration.
            // This prevents the gamepad values from changing between being
            // used and stored in previousGamepad1/2.
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // Main teleop loop goes here
            //Claw
            if (currentGamepad1.circle && !previousGamepad1.circle)
            {
                if (claw.getState() != Claw.State.Closed)
                {
                    claw.close();
                }
                else if (claw.getState() != Claw.State.Open)
                {
                    claw.open();
                }
            }
        }
    }
}
