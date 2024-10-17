package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.profile.VelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.Claw
import org.firstinspires.ftc.teamcode.modules.robot.Slides
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.sequencesegment.FunctionSegment


@Autonomous
class LuaRRTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);

		val claw = Claw(hardwareMap.servo.get("claw"));

		val slides = Slides(hardwareMap.dcMotor.get("slide"));

		val path = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(
				Vector2d(0.0, -30.0), velOverride(DriveConstants.MAX_VEL / 2), accelOverride()
			).build();


		val path2 = drive.trajectorySequenceBuilder(path.end()).lineToLinearHeading(
			Pose2d(48.0, -55.0, Math.toRadians(90.0)),
			velOverride(DriveConstants.MAX_VEL / 2, DriveConstants.MAX_ANG_VEL / 2),
			accelOverride()
		).build();

		val path3 = drive.trajectorySequenceBuilder(path2.end()).lineToLinearHeading(
				path2.start(), velOverride(DriveConstants.MAX_VEL / 2, DriveConstants.MAX_ANG_VEL / 2), accelOverride()
			).build();

		drive.poseEstimate = path.start();

		waitForStart();

		claw.close();

		slides.runToPosition(-1400);

		drive.followTrajectorySequence(path);

		slides.runToPosition(-1000, 0.5)
		while(slides.isBusy());
		claw.open();

		slides.runToPosition(0);

		drive.followTrajectorySequence(path2);

		claw.close();
		delay(1.0);

		slides.runToPosition(-1400);
		drive.followTrajectorySequence(path3);
	}

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}