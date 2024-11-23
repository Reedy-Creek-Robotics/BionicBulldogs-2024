package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import kotlin.math.PI

@Autonomous
class SampleAuto: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"));
		val outtake = Outtake(hardwareMap);
		val intake = Intake(hardwareMap.crservo.get("rotator0"), null, null);
		val claw = SpeciminClaw(hardwareMap.servo.get("claw"));
		val slide = Slide(hardwareMap.dcMotor.get("slide") as DcMotorEx);
		val arm = Arm(hardwareMap.servo.get("arm"));

		val preload = drive.trajectorySequenceBuilder(Pose2d(-6.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(Vector2d(-6.0, -28.0)).build();
		val samp1 =
			drive.trajectorySequenceBuilder(preload.end()).lineToLinearHeading(Pose2d(-36.0, -36.0, PI))
				.lineToLinearHeading(Pose2d(-36.0, -22.0, PI)).build();
		val toScore = drive.trajectorySequenceBuilder(samp1.end())
			.lineToLinearHeading(Pose2d(-52.5, -52.0, Math.toRadians(45.0))).build();


//		val park = drive.trajectorySequenceBuilder(samp2.end())
//			.lineToLinearHeading(Pose2d(-27.0, -10.0, Math.toRadians(180.0))).build();

		drive.poseEstimate = preload.start();

		outtake.armDown();
		outtake.bucketDown();
		claw.close();
		arm.down();

		waitForStart();

		slide.runToPosition(-1400);
		drive.followTrajectorySequence(preload);

		slide.runToPosition(-1000, 0.5)
		while(slide.busy());
		claw.open();
		slide.runToPosition(0);

		drive.followTrajectorySequence(samp1);

		intake.forward();
		hslide.gotoPos(0.65);

		delay(2.0);

//        drive.followTrajectorySequence(samp2);
	}

	fun delay(time: Double)
	{
		val elapsedTime = ElapsedTime();
		elapsedTime.reset();
		while(elapsedTime.seconds() < time);
	}
}