package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.Logging
import org.firstinspires.ftc.teamcode.modules.robot.*
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

@Autonomous
@Config
class SampleAutoV4: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"));
		val outtake = Outtake(hardwareMap);
		val slide = Slide(hardwareMap);
		val sampleOuttake = SampleOuttake(slide, outtake);
		val intake = Intake(hardwareMap);
		val claw = SpeciminClaw(hardwareMap);
		val arm = Arm(hardwareMap.servo.get("arm"));
		val specimenClaw = SpecimenOuttake(claw, slide);

		val log = Logging();
		log.state.set("toBasket");

		val startPos = pos(-42.0, -64.5, 90);
		val scorePos = pos(-57.5, -57.5, 45);
		val samplePos = pos(-44.0, -47.0, 0);
		val samplePos2 = pos(-51.0, -47.0, 0);
		val parkPos = pos(-27.0, -10.0, 0);

		val trajectory = drive.trajectorySequenceBuilder(startPos)
			.lineToLinearHeading(scorePos, velOverride(), accelOverride(maxAccel = 0.8))
			.addDisplacementMarker(
				fun()
				{
					drive.setDriveSignal(DriveSignal());
					sampleOuttake.score();

					hslide.gotoPos(HSlide.min);
					intake.rotatorLeft();

					while(sampleOuttake.isBusy())
					{
						drive.localizer.update();
						drive.log(log);
						sampleOuttake.update();
						log.update();
					}
					log.state.set("toSample");
				}
			)
			.lineToLinearHeading(samplePos, velOverride(), accelOverride(maxAccel = 0.8))
			.addDisplacementMarker(
				fun()
				{
					intake.forward();
					log.state.set("intaking");
				}
			)
			.lineToLinearHeading(samplePos2, velOverride(), accelOverride(maxAccel = 0.8))
			.addTemporalMarker(
				fun()
				{
					drive.setDriveSignal(DriveSignal());
					hslide.score();
					intake.zeroRotator();
					intake.stop();
					arm.up();
				}
			)
			.UNSTABLE_addTemporalMarkerOffset(1.0,
				fun()
				{
					intake.reverse();
				}
			)
			.UNSTABLE_addTemporalMarkerOffset(2.0,
				fun()
				{
					intake.stop();
					arm.down();
					hslide.zero();
					log.state.set("toBasket");
				}
			)
			.waitSeconds(2.0)
			.lineToLinearHeading(scorePos)
			.addDisplacementMarker(
				fun()
				{
					drive.setDriveSignal(DriveSignal());
					sampleOuttake.score();

					log.state.set("scoring");

					while(sampleOuttake.isBusy())
					{
						drive.localizer.update();
						drive.log(log);
						sampleOuttake.update();
						log.update();
					}
					log.state.set("toSample");
				}
			)
			//.lineToLinearHeading(samplePos)
			//.lineToLinearHeading(scorePos)
			//.lineToLinearHeading(parkPos)
			.build();

		drive.poseEstimate = trajectory.start();

		specimenClaw.init();

		claw.open();
		arm.down();
		hslide.zero();
		sampleOuttake.init();
		intake.zeroRotator();

		waitForStart();

		sampleOuttake.up();
		drive.followTrajectorySequenceAsync(trajectory);
		while(drive.isBusy && opModeIsActive())
		{
			sampleOuttake.update();
			drive.update();
			drive.log(log);
			log.update();
		}
		delay(1.0);
	}
}