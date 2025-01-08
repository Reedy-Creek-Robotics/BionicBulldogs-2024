package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.Logging
import org.firstinspires.ftc.teamcode.modules.drive.rotPos
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

@Autonomous
@Config
class SpecimenAutoV1_1: LinearOpMode()
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
		val intake = Intake(hardwareMap);
		val arm = Arm(hardwareMap.servo.get("arm"));

		hslide.zero();
		claw.close();
		outtake.bucketDown();
		outtake.armDown();
		arm.down();
		intake.zeroRotator();

		val startPose = pos(8.0, -64.25, 180);
		val scorePos = pos(8.0, -32.0, 180);
		val wallPose = pos(42.5, -62.0, 0);

		log.state.set("toChamber");

		val path = drive.trajectorySequenceBuilder(startPose)
			.addDisplacementMarker(fun()
			{
				slides.raise();
			})
			.lineToLinearHeading(scorePos, velOverride(), accelOverride(maxAccel = 0.8))
			.addDisplacementMarker(fun()
			{
				specimenOuttake.score();
				log.state.set("scoring");
				while(specimenOuttake.isBusy())
				{
					drive.localizer.update();
					drive.log(log);
					specimenOuttake.update();
					log.update();
				}
				log.state.set("toWall");
			})
			.lineToLinearHeading(Pose2d(wallPose.x, wallPose.y + 4, wallPose.heading), velOverride(), accelOverride(maxAccel = 0.6))
			.lineToLinearHeading(wallPose, velOverride(), accelOverride(maxAccel = 0.6))
			.addDisplacementMarker(fun()
			{
				log.state.set("collecting");
				specimenOuttake.collect();
				while(specimenOuttake.isBusy())
				{
					drive.localizer.update();
					drive.log(log);
					specimenOuttake.update();
					log.update();
				}
				specimenOuttake.waitUntilIdle();
				log.state.set("toChamber");
			})
			.lineToLinearHeading(
				Pose2d(scorePos.x - 1.0, scorePos.y - 6, scorePos.heading),
				velOverride(),
				accelOverride(maxAccel = 0.6)
			)
			.lineToLinearHeading(
				Pose2d(scorePos.x - 1.0, scorePos.y - 0.5, scorePos.heading),
				velOverride(),
				accelOverride(maxAccel = 0.6)
			)
			.addDisplacementMarker(fun()
			{
				//drive.setDriveSignal(DriveSignal());
				specimenOuttake.score();
				specimenOuttake.waitUntilIdle();
				log.state.set("toSamples");
			})
			.setTangent(rotation(-180))
			.splineToLinearHeading(pos(33.0, -33.0, 45), rotation(55))
			.addTemporalMarker(fun()
			{
				log.state.set("collect");
				hslide.gotoPos(HSlide.min);

				intake.forward();
			})
			/*.waitSeconds(0.5)
			.lineToLinearHeading(pos(36.0, -48.0, 135))
			.addTemporalMarker(fun()
			{
				intake.reverse();
				arm.up();
				delay(0.5);
			})*/

			.build();

		drive.poseEstimate = path.start();

		waitForStart();

		drive.followTrajectorySequenceAsync(path);
		while(opModeIsActive() && drive.isBusy)
		{
			drive.update();
			drive.log(log);
			log.update();
		}
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < 1.0);
		rotPos = Math.toRadians(180.0);
	}
}
