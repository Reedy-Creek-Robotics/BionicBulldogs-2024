package org.firstinspires.ftc.teamcode.modules.actions

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

lateinit var intake: Intake;
lateinit var arm: Arm;
lateinit var hSlide: HSlide;
lateinit var slide: Slide;
lateinit var specimenClaw: SpeciminClaw;
lateinit var outtake: Outtake;
lateinit var specimenOuttake: SpecimenOuttake;
lateinit var sampleOuttake: SampleOuttake;
lateinit var drive: MecanumDrive;

fun initComponents(hardwareMap: HardwareMap)
{
	intake = Intake(hardwareMap);
	arm = Arm(hardwareMap);
	hSlide = HSlide(hardwareMap);
	slide = Slide(hardwareMap);
	specimenClaw = SpeciminClaw(hardwareMap);
	outtake = Outtake(hardwareMap);
	specimenOuttake = SpecimenOuttake(specimenClaw, slide);
	sampleOuttake = SampleOuttake(slide, outtake);
	drive = MecanumDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0));

	intake.zeroRotator();
	arm.up();
	hSlide.zero();
	specimenClaw.close();
	sampleOuttake.init();
}