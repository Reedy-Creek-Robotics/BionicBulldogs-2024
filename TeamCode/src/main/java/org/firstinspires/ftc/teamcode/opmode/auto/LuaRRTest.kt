package org.firstinspires.ftc.teamcode.opmode.auto

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.robot.HSlide
import org.firstinspires.ftc.teamcode.modules.robot.Outtake
import org.firstinspires.ftc.teamcode.modules.robot.SpeciminClaw
import org.firstinspires.ftc.teamcode.modules.robot.Slide
import org.firstinspires.ftc.teamcode.opmode.telop.MainTelop
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive


@Autonomous
class LuaRRTest: LinearOpMode()
{
	override fun runOpMode()
	{
		val drive = SampleMecanumDrive(hardwareMap);

		val speciminClaw = SpeciminClaw(hardwareMap.servo.get("claw"));

		val slides = Slide(hardwareMap.dcMotor.get("slide") as DcMotorEx);

		val outtake = Outtake(hardwareMap);
		val hslide = HSlide(hardwareMap.servo.get("hslide"))

		val path = drive.trajectorySequenceBuilder(Pose2d(0.0, -60.0, Math.toRadians(-90.0)))
			.lineToConstantHeading(
				Vector2d(0.0, -28.0)
			).build();


		val path2 = drive.trajectorySequenceBuilder(path.end()).lineToLinearHeading(
			Pose2d(38.0, -55.0, Math.toRadians(90.0)),
		).build();

		val path3 = drive.trajectorySequenceBuilder(path2.end()).lineToLinearHeading(
			Pose2d(path2.start().x, path2.start().y - 6, path2.start().heading)
		).lineToConstantHeading(Vector2d(path2.start().x, path2.start().y)).build();

		drive.poseEstimate = path.start();

		waitForStart();

		hslide.zero();
		outtake.armDown();
		outtake.bucketDown();

		speciminClaw.close();

		slides.runToPosition(-1400);

		drive.followTrajectorySequence(path);

		slides.runToPosition(-1000, 0.5)
		while(slides.busy());
		speciminClaw.open();

		slides.runToPosition(0);
		while(slides.getPos() < -250)
		{
			if(slides.getPos() > -1000)
			{
				speciminClaw.open();
			}
		}

		drive.followTrajectorySequence(path2);

		speciminClaw.close();
		delay(1.0);

		slides.runToPosition(-1400);
		drive.followTrajectorySequence(path3);

		slides.runToPosition(-900, 0.5)
		while(slides.busy());
		speciminClaw.open();
		slides.lower();
		while(slides.getPos() < -250);
	}

	private fun delay(time: Double)
	{
		val e = ElapsedTime();
		e.reset();
		while(e.seconds() < time);
	}
}