package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.modules.robot.SpecimenOuttake
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive

@Autonomous
class SpecimenAuto: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);

		val claw = SpeciminClaw(hardwareMap);
		val slide = Slide(hardwareMap);
		val specimenOuttake = SpecimenOuttake(claw, slide);

		val path = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(
				Vector2d(0.0, -28.0)
			).build();

		val path2 = drive.trajectorySequenceBuilder(path.end()).lineToLinearHeading(
			Pose2d(38.0, -55.0, Math.toRadians(90.0)),
		).build();

		val path3 = drive.trajectorySequenceBuilder(path2.end()).lineToLinearHeading(
			Pose2d(path2.start().x, path2.start().y - 2, path2.start().heading)
		).build();

		drive.poseEstimate = path.start();

		waitForStart();

		specimenOuttake.collect();

		drive.followTrajectorySequence(path);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();

		drive.followTrajectorySequence(path2);

		claw.close();
		delay(1.0);

		specimenOuttake.collect();
		drive.followTrajectorySequence(path3);

		specimenOuttake.score();
		specimenOuttake.waitUntilIdle();
	}

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}