package com.example.meepmeep

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.rowlandhall.meepmeep.MeepMeep
import org.rowlandhall.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder
import org.rowlandhall.meepmeep.roadrunner.DriveShim
import org.rowlandhall.meepmeep.roadrunner.trajectorysequence.TrajectorySequence

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
	val sampleScorePos = pos(-55.0, -55.0, 45);

	val builder = drive.trajectorySequenceBuilder(pos(-startX, startY, 180));
	builder.lineToConstantHeading(Vector2d(-startX, specimineScoreY));
	builder.waitSeconds(1.0);

	builder.splineToLinearHeading(pos(-sampleX[0], sampleY, -90), Math.toRadians(90.0));
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

fun main()
{
	val meepMeep = MeepMeep(800);

	val specimineBotBuilder = DefaultBotBuilder(meepMeep);
	specimineBotBuilder.setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0);
	specimineBotBuilder.setDimensions(15.0, 13.0);
	val specimineBot = specimineBotBuilder.followTrajectorySequence {drive: DriveShim -> speciminSide(drive)};

	val sampleBotBuilder = DefaultBotBuilder(meepMeep);
	sampleBotBuilder.setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0);
	sampleBotBuilder.setDimensions(15.0, 13.0);
	sampleBotBuilder.setColorScheme(ColorSchemeBlueDark());
	val sampleBot = sampleBotBuilder.followTrajectorySequence {drive: DriveShim -> sampleSide(drive)};

	meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK);
	meepMeep.setDarkMode(true);
	meepMeep.setBackgroundAlpha(0.95f);
	meepMeep.addEntity(sampleBot);
	meepMeep.addEntity(specimineBot);
	meepMeep.start();
}