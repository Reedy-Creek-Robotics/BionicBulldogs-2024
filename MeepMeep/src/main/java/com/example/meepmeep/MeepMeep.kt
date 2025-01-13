package com.example.meepmeep

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.rowlandhall.meepmeep.MeepMeep
import org.rowlandhall.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder
import org.rowlandhall.meepmeep.roadrunner.DriveShim
import org.rowlandhall.meepmeep.roadrunner.trajectorysequence.TrajectorySequence
import kotlin.math.PI

val startX = 10.0;
val startY = -60.0;

val sampleX = arrayOf(36.0, 48.0, 60.0);
val sampleY = -23.0;

val specimineScoreY = -30.0;

val noPartner = true;

fun rotation(angle: Int): Double
{
	return Math.toRadians(-(angle.toDouble() - 90))
}

fun pos(x: Double, y: Double, angle: Int): Pose2d
{
	return Pose2d(x, y, rotation(angle));
}


fun speciminSide(drive: DriveShim): TrajectorySequence
{
	val scoreY = -40.0;
	val collectY = -55.0;

	val builder = drive.trajectorySequenceBuilder(pos(startX, startY, -180));

	builder.lineToConstantHeading(Vector2d(startX, specimineScoreY));
	builder.waitSeconds(1.0);

	if(noPartner)
	{
		builder.lineToLinearHeading(pos(sampleX[0], collectY, 0));
		builder.waitSeconds(1.0);
		builder.lineToLinearHeading(pos(startX, specimineScoreY, 180));
		builder.waitSeconds(1.0);
	}

	builder.splineToLinearHeading(pos(sampleX[0], sampleY, -270), rotation(0))
	builder.waitSeconds(1.0);

	builder.lineToLinearHeading(pos(sampleX[0], scoreY, 180));
	builder.waitSeconds(1.0);

	for(i in 1 .. 2)
	{
		builder.lineToLinearHeading(pos(sampleX[i], sampleY, 90));
		builder.waitSeconds(1.0);

		builder.lineToLinearHeading(pos(sampleX[i], scoreY, 180));
		builder.waitSeconds(1.0);
	}

	builder.lineToLinearHeading(pos(sampleX[0], collectY, 0));
	builder.waitSeconds(1.0);

	for(i in 0 .. 1)
	{
		builder.lineToLinearHeading(pos(startX, specimineScoreY, 180));
		builder.waitSeconds(1.0);
		builder.lineToLinearHeading(pos(sampleX[0], collectY, 0));
		builder.waitSeconds(1.0);
	}

	return builder.build();
}

fun sampleSide(drive: DriveShim): TrajectorySequence
{
	val sampleScorePos = pos(-55.0, -50.0, 45);

	val builder = drive.trajectorySequenceBuilder(pos(-startX, startY, 180));
	builder.lineToConstantHeading(Vector2d(-startX, specimineScoreY));
	builder.waitSeconds(1.0);

	//two below lines could replace spline in sample
	builder.setTangent(-115.0);
	builder.splineToLinearHeading(pos(-sampleX[0], sampleY, -90), Math.toRadians(115.0));
	builder.waitSeconds(1.0);

	builder.lineToLinearHeading(sampleScorePos);
	builder.waitSeconds(1.0);

	for(i in 1..2)
	{
		builder.lineToLinearHeading(pos(-sampleX[i], sampleY, -90));
		builder.waitSeconds(1.0);

		builder.lineToLinearHeading(sampleScorePos);
		builder.waitSeconds(1.0);
	}

	builder.lineToLinearHeading(Pose2d(-27.0, -10.0, Math.toRadians(90.0)));

	return builder.build();
}

fun SpecimenAutoV1_1(drive:DriveShim): TrajectorySequence
{

	val startPose = pos(-8.0, 64.25, 0);
	val scorePos = pos(-8.0, 32.5, 0);
	val wallPose = pos(-42.5, 58.5, 180);

	return drive.trajectorySequenceBuilder(startPose)
		.lineToLinearHeading(scorePos)
		.lineToLinearHeading(wallPose)
		.lineToLinearHeading(Pose2d(scorePos.x, scorePos.y + 6, scorePos.heading))
		.lineToLinearHeading(Pose2d(scorePos.x, scorePos.y + 1, scorePos.heading))
		.build();
}

fun SampleAutoV4(drive:DriveShim): TrajectorySequence
{

	val startPos = pos(42.0, 64.5, 180);
	val scorePos = pos(57.5, 57.5, 225);
	val samplePos = pos(44.0, 47.0, 180);
	val samplePos2 = pos(52.0, 47.0, 180);
	val samplePos3 = pos(54.0, 47.0, 180);
	val samplePos4 = pos(63.0, 47.0, 180);
	val samplePos5 = pos(53.0, 30.0, 90);
	val parkPos = pos(27.0, 10.0, 180);

	return drive.trajectorySequenceBuilder(startPos)
		.lineToLinearHeading(scorePos)
		.lineToLinearHeading(Pose2d(samplePos.x, samplePos.y + 0.5, samplePos.heading))
		.lineToLinearHeading(samplePos2)
		.waitSeconds(1.0)
		.lineToLinearHeading(scorePos)
		.lineToLinearHeading(samplePos3)
		.lineToLinearHeading(samplePos4)
		.waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(scorePos.x - 0.5, scorePos.y - 0.5, scorePos.heading))
		.lineToLinearHeading(samplePos5)
		.waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(scorePos.x - 1, scorePos.y - 1, scorePos.heading))
		//.lineToLinearHeading(parkPos)
		.build();
}

fun e(drive: DriveShim): TrajectorySequence
{
	return drive.trajectorySequenceBuilder(Pose2d(-6.0, -60.0, Math.toRadians(-90.0)))
		.lineToConstantHeading(Vector2d(-6.0, -28.0))
		.lineToLinearHeading(Pose2d(-30.0, -40.0, PI))
		.lineToLinearHeading(Pose2d(-36.0, -23.0, PI))
		.lineToLinearHeading(Pose2d(-53.5, -52.0, Math.toRadians(45.0)))
		.lineToLinearHeading(Pose2d(-44.0, -23.0, PI))
		.lineToLinearHeading(Pose2d(-53.5, -52.0, Math.toRadians(45.0)))
		.lineToLinearHeading(Pose2d(-27.0, -10.0, Math.toRadians(90.0))).build();
}

fun main()
{
	val meepMeep = MeepMeep(800);

	val specimineBotBuilder = DefaultBotBuilder(meepMeep);
	specimineBotBuilder.setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0);
	specimineBotBuilder.setDimensions(15.0, 13.0);
	val specimineBot = specimineBotBuilder.followTrajectorySequence {drive: DriveShim -> SpecimenAutoV1_1(drive) };

	val sampleBotBuilder = DefaultBotBuilder(meepMeep);
	sampleBotBuilder.setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0);
	sampleBotBuilder.setDimensions(15.0, 13.0);
	sampleBotBuilder.setColorScheme(ColorSchemeBlueDark());
	val sampleBot = sampleBotBuilder.followTrajectorySequence {drive: DriveShim -> SampleAutoV4(drive) }

	meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK);
	meepMeep.setDarkMode(true);
	meepMeep.setBackgroundAlpha(0.95f);
	meepMeep.addEntity(sampleBot);
	meepMeep.addEntity(specimineBot);
	meepMeep.start();
}
