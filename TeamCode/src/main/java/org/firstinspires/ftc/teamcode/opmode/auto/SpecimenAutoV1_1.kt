package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.Logging
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

@Autonomous
@Config
class SpecimenAutoV1_1 : LinearOpMode()
{
	override fun runOpMode()
	{
		val log = Logging();
		
		val drive = SampleMecanumDrive(hardwareMap);
		val claw = SpeciminClaw(hardwareMap);
		val slides = Slide(hardwareMap);
		val outtake = Outtake(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"));
		val specimenOuttake = SpecimenOuttake(claw, slides);
		
		hslide.zero();
		claw.close();
		outtake.bucketDown();
		outtake.armDown();
		
		val startPose = pos(7.5, -65.5, 0);
		val scorePos = pos(7.5, -36.0, 0);
		val wallPose = pos(42.5, -65.0, 180);
		
		val path = drive.trajectorySequenceBuilder(startPose)
			.addDisplacementMarker(fun()
			{
				slides.raise();
			})
			.lineToLinearHeading(scorePos, velOverride(), accelOverride(maxAccel = 0.6))
			.addDisplacementMarker(fun()
			{
				drive.setDriveSignal(DriveSignal());
				specimenOuttake.score();
				specimenOuttake.waitUntilIdle();
			})
			.lineToLinearHeading(wallPose, velOverride(), accelOverride(maxAccel = 0.6))
			.addDisplacementMarker(fun()
			{
				drive.setDriveSignal(DriveSignal());
				specimenOuttake.collect();
				specimenOuttake.waitUntilIdle();
			})
			.lineToLinearHeading(scorePos, velOverride(), accelOverride(maxAccel = 0.6))
			.addDisplacementMarker(fun()
			{
				drive.setDriveSignal(DriveSignal());
				specimenOuttake.score();
				specimenOuttake.waitUntilIdle();
			})
			.build();
		
		drive.poseEstimate = path.start();
		
		waitForStart();
		
		drive.followTrajectorySequenceAsync(path);
		while (opModeIsActive() && drive.isBusy)
		{
			drive.update();
			log.posX.set(drive.poseEstimate.x);
			log.posY.set(drive.poseEstimate.y);
			log.posH.set(drive.poseEstimate.heading);
			log.update();
		}
	}
}
