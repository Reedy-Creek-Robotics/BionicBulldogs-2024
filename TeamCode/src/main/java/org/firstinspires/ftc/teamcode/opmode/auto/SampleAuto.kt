package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

class SampleAuto: LinearOpMode() {
    override fun runOpMode() {
        val drive = SampleMecanumDrive(hardwareMap);
        val hslide = HSlide(hardwareMap.servo.get("hslide"));
        val outtake = Outtake(hardwareMap);
        val intake = Intake(hardwareMap.crservo.get("rotator0"), null, null);
        val claw = SpeciminClaw(hardwareMap.servo.get("claw"));
        val slide = Slide(hardwareMap.dcMotor.get("slide") as DcMotorEx);

        val preload = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
            .lineToConstantHeading(Vector2d(-6.0, -30.0)).build();
        val samp1 = drive.trajectorySequenceBuilder(preload.end()).lineToLinearHeading(Pose2d(-48.0, -36.0, Math.toRadians(90.0)))
            .lineToLinearHeading(Pose2d(-60.0, -55.0, Math.toRadians(45.0))).build();
        val samp2 = drive.trajectorySequenceBuilder(samp1.end()).lineToLinearHeading(Pose2d(-58.0, -36.0, Math.toRadians(90.0)))
            .lineToLinearHeading(Pose2d(-60.0, -55.0, Math.toRadians(45.0))).build();
        val park = drive.trajectorySequenceBuilder(samp2.end()).lineToLinearHeading(Pose2d(-27.0, -10.0, Math.toRadians(180.0))).build();

        drive.poseEstimate = preload.start();

        waitForStart();

        claw.close();

        slide.runToPosition(-1400);

        drive.followTrajectorySequence(preload);

        slide.runToPosition(-1000, 0.5)
        while(slide.busy());
        claw.open();

        slide.runToPosition(0);

    }
}