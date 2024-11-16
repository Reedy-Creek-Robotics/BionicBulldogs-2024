package com.example.meepmeep

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.rowlandhall.meepmeep.MeepMeep
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder
import org.rowlandhall.meepmeep.roadrunner.DriveShim
import org.rowlandhall.meepmeep.roadrunner.trajectorysequence.TrajectorySequence


fun speciminSide(drive: DriveShim): TrajectorySequence
{
	return drive.trajectorySequenceBuilder(Pose2d(6.0, -66.0, Math.toRadians(-90.0)))
		.lineToConstantHeading(Vector2d(6.0, -30.0)).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(48.0, -60.0, Math.toRadians(90.0))).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(12.0, -30.0, Math.toRadians(-90.0))).waitSeconds(1.0)
		.lineToConstantHeading(Vector2d(48.0, -60.0)).build()
}

fun sampleSide(drive: DriveShim): TrajectorySequence
{
	return drive.trajectorySequenceBuilder(Pose2d(-6.0, -66.0, Math.toRadians(-90.0)))
		.lineToConstantHeading(Vector2d(-6.0, -30.0)).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(-48.0, -36.0, Math.toRadians(90.0))).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(-57.0, -57.0, Math.toRadians(45.0))).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(-60.0, -36.0, Math.toRadians(90.0))).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(-57.0, -57.0, Math.toRadians(45.0))).waitSeconds(1.0)
		.lineToLinearHeading(Pose2d(-30.0, 0.0, Math.toRadians(90.0))).build();
}

fun main()
{
	val meepMeep = MeepMeep(800);

	val botBuilder = DefaultBotBuilder(meepMeep);
	botBuilder.setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0);
	botBuilder.setDimensions(12.0, 12.0)
	val bot = botBuilder.followTrajectorySequence {drive: DriveShim -> sampleSide(drive)};

	meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK);
	meepMeep.setDarkMode(true);
	meepMeep.setBackgroundAlpha(0.95f);
	meepMeep.addEntity(bot);
	meepMeep.start();
}