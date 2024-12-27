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
class SpecimenAutoV2: LinearOpMode()
{
	val startX = 7.5;
	val startY = -65.5;

	val sampleX = arrayOf(36.5, 46.0, 56.0);
	val sampleY = -30.0;

	val wallX = 42.5;
	val wallY = -65.0;

	val specimineScoreY = -35.0;

	val sampleScoreX = 42.0;
	val sampleScoreY = -41.0;

	val noPartner = true;

	var scoreCount = 0;

	fun rotation(angle: Int): Double
	{
		return Math.toRadians(-(angle.toDouble() - 90))
	}

	fun pos(x: Double, y: Double, angle: Int): Pose2d
	{
		return Pose2d(x, y, rotation(angle));
	}

	fun gotoChamber(builder: TrajectorySequenceBuilder, yOffset: Double = 0.0)
	{
		builder.lineToLinearHeading(pos(startX, specimineScoreY - 4 + yOffset, 180));
		builder.lineToConstantHeading(
			Vector2d(startX - scoreCount * 4, specimineScoreY + yOffset), velOverride(), accelOverride(maxAccel = 30.0)
		);
		scoreCount++;
	}

	fun getPaths(drive: SampleMecanumDrive): List<TrajectorySequence>
	{
		val paths = ArrayList<TrajectorySequence>();

		var builder = drive.trajectorySequenceBuilder(pos(startX, startY, -180));

    //to score specimen
		builder.lineToConstantHeading(Vector2d(startX, specimineScoreY));

		paths.add(builder.build());

    //to grab sample
		builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
		builder.setTangent(rotation(180));
		builder.splineToLinearHeading(pos((startX + sampleX[0]) / 2, -36.0, -225), rotation(90))
		builder.setTangent(rotation(90));
		builder.splineToLinearHeading(
			pos(sampleX[0], sampleY, 88), rotation(0), velOverride(), accelOverride(maxAccel = 30.0)
		);
		//TODO(Add below line, remove top 4 lines)
		//builder.splineToLinearHeading(pos(sampleX[0], sampleY, -270), rotation(0))
		paths.add(builder.build());

    //to score sample 
		builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
		builder.lineToLinearHeading(pos(sampleScoreX, sampleScoreY, 165));
		paths.add(builder.build());

    //to grab specimen
		builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
		builder.lineToLinearHeading(pos(wallX, wallY + 3, 0));
		builder.lineToLinearHeading(
			pos(wallX, wallY, 0), velOverride(), accelOverride(maxAccel = 30.0)
		);
		paths.add(builder.build());

    //to score specimen
		builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
		gotoChamber(builder, -4.5);
		paths.add(builder.build());

    //to grab specimen
		builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
		builder.lineToLinearHeading(pos(wallX, wallY + 3, 0));
		builder.lineToLinearHeading(
			pos(wallX, wallY, 0), velOverride(), accelOverride(maxAccel = 30.0)
		);
		paths.add(builder.build());

    //to score specimen
		builder = drive.trajectorySequenceBuilder(paths[paths.size - 1].end());
		gotoChamber(builder, -4.5);
		paths.add(builder.build());

		return paths;

		for(i in 1 .. 2)
		{
			builder.lineToLinearHeading(pos(sampleX[i], sampleY, 90));
			builder.waitSeconds(1.0);

			builder.lineToLinearHeading(pos(sampleX[i], specimineScoreY, 180));
			builder.waitSeconds(1.0);
		}

		builder.lineToLinearHeading(pos(sampleX[0], wallY, 0));
		builder.waitSeconds(1.0);

		for(i in 0 .. 2)
		{
			gotoChamber(builder);
			builder.waitSeconds(1.0);
			builder.lineToLinearHeading(pos(sampleX[0], wallY, 0));
			builder.waitSeconds(1.0);
		}

		return paths;
	}

	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);

		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		val specimenOuttake = SpecimenOuttake(claw, slide);

		val outtake = Outtake(hardwareMap);
		val sampleOuttake = SampleOuttake(slide, outtake);

		val arm = Arm(hardwareMap.servo.get("arm"));
		val intake = Intake(
			hardwareMap.crservo.get("rotator0"), null, null
		);

		val hSlide = HSlide(hardwareMap.servo.get("hslide"));

		val path = getPaths(drive);

		drive.poseEstimate = path[0].start();

		hSlide.zero();
		arm.down();

		specimenOuttake.init();
		sampleOuttake.init();
		claw.close();

    var i = 0;

		waitForStart();

		specimenOuttake.collect();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path[i++]);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path[i++]);

		intake.forward();

		hSlide.gotoPos(0.5);

		rotPos = drive.localizer.poseEstimate.heading;

		val elapsedTime = ElapsedTime();
		elapsedTime.reset();

		while(elapsedTime.seconds() < 1.0);

		hSlide.zero();
		intake.stop();

		elapsedTime.reset();
		while(elapsedTime.seconds() < 1.0);

		drive.followTrajectorySequence(path[i++]);

		hSlide.gotoPos(HSlide.min);

		elapsedTime.reset();
		while(elapsedTime.seconds() < 1.0);

		intake.reverse();

		elapsedTime.reset();
		while(elapsedTime.seconds() < 0.25);

		arm.up();

		elapsedTime.reset();
		while(elapsedTime.seconds() < 1.0);

		intake.stop();
		hSlide.zero();
		arm.down();

		drive.followTrajectorySequence(path[i++]);

		specimenOuttake.collect();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path[i++]);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path[i++]);

		specimenOuttake.collect();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path[6]);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		elapsedTime.reset();
		while(elapsedTime.seconds() < 1.0);
	}
}
