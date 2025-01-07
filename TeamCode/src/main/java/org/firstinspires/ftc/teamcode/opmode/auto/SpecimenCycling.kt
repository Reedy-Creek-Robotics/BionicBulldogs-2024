package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.Arm
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Intake
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.SampleOuttake
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder

@Autonomous
class SpecimenCycling: LinearOpMode()
{
  lateinit var drive: SampleMecanumDrive;
  lateinit var claw: SpeciminClaw;
  lateinit var slide: Slide;
  lateinit var specimenOuttake: SpecimenOuttake;
  lateinit var sampleOuttake: SampleOuttake;
  lateinit var outtake: Outtake;
  lateinit var arm: Arm;
  lateinit var intake: Intake;
  lateinit var hSlide: HSlide;

  override fun runOpMode()
  {
		drive = SampleMecanumDrive(hardwareMap);

		claw = SpeciminClaw(hardwareMap);
		slide = Slide(hardwareMap);
		specimenOuttake = SpecimenOuttake(claw, slide);

		outtake = Outtake(hardwareMap);
		sampleOuttake = SampleOuttake(slide, outtake);

		arm = Arm(hardwareMap.servo.get("arm"));
		intake = Intake(hardwareMap);

		hSlide = HSlide(hardwareMap.servo.get("hslide"));

    val trajectory = drive.trajectorySequenceBuilder(pos(30.0, -40.0, 45))
    .turn(Math.toRadians(-90.0))
    .build();
    val trajectory2 = drive.trajectorySequenceBuilder(trajectory.end())
    .lineToLinearHeading(pos(42.0, -38.0, 40))
    .build();
    val trajectory3 = drive.trajectorySequenceBuilder(trajectory2.end())
    .turn(Math.toRadians(-93.0))
    .build();
    val trajectory4 = drive.trajectorySequenceBuilder(trajectory3.end())
    .lineToLinearHeading(pos(54.0, -36.0, 40))
    .build();
    val trajectory5 = drive.trajectorySequenceBuilder(trajectory4.end())
    .turn(Math.toRadians(-103.0))
    .build();


		hSlide.zero();
		arm.down();

    drive.poseEstimate = trajectory.start();

		specimenOuttake.init();
		sampleOuttake.init();
		claw.close();
    
    intake.zeroRotator();

		waitForStart();

    moveSample(trajectory);
    arm.down();
    hSlide.zero();
    drive.poseEstimate = trajectory2.start();
    drive.followTrajectorySequence(trajectory2);
    moveSample(trajectory3);
    arm.down();
    hSlide.zero();
    drive.poseEstimate = trajectory4.start();
    drive.followTrajectorySequence(trajectory4);
    moveSample(trajectory5, true);
  }

  fun moveSample(turnTrajectory: TrajectorySequence, retractSlides: Boolean = false)
  {
    hSlide.gotoPos(HSlide.min);

    intake.forward();
    
    delay(0.25);
    if(retractSlides)
    {
      delay(0.25);
      hSlide.zero();
    }

    drive.followTrajectorySequence(turnTrajectory);

    if(retractSlides)
    {
      hSlide.gotoPos(HSlide.min);
      delay(0.25);
    }

    intake.reverse();
    arm.up();
    delay(0.25);
  }
}
