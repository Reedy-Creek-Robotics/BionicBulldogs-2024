package org.firstinspires.ftc.teamcode.modules.drive

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.modules.Vec2
import org.firstinspires.ftc.teamcode.modules.hardware.MotorGroup
import org.firstinspires.ftc.teamcode.modules.robot.Acceleration
import org.firstinspires.ftc.teamcode.opmode.config.HDriveConfig
import java.lang.RuntimeException
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Config
class HDrive(config: HDriveConfig)
{
	private var motors: MotorGroup;
	private var frontLeft: DcMotor;
	private var frontRight: DcMotor;
	private var backLeft: DcMotor;
	private var backRight: DcMotor;
	private var f = 0f;
	private var r = 0f;
	private var localizer: Localizer? = null;
	private var acceleration: Acceleration = Acceleration();

	init
	{
		frontLeft = config.getFrontLeft();
		frontRight = config.getFrontRight();
		backLeft = config.getBackLeft();
		backRight = config.getBackRight();

		frontLeft.direction = DcMotorSimple.Direction.FORWARD;
		frontRight.direction = DcMotorSimple.Direction.REVERSE;
		backLeft.direction = DcMotorSimple.Direction.FORWARD;
		backRight.direction = DcMotorSimple.Direction.REVERSE;

		motors = MotorGroup();
		motors.addMotor(frontLeft);
		motors.addMotor(frontRight);
		motors.addMotor(backLeft);
		motors.addMotor(backRight);

		motors.resetEncoders();
		motors.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		motors.setZeroPower(DcMotor.ZeroPowerBehavior.BRAKE);
	}

	fun setLocalizer(localizer2: Localizer)
	{
		localizer = localizer2;
	}

	fun setPosEstimate(pos: Pose2d)
	{
		localizer!!.poseEstimate = pos;
	}

	fun resetAccel()
	{
		acceleration.resetTime();
	}

	/**
	 * field relative drive
	 * @param forward positive is forward (-1, 1)
	 * @param strafe positive is right (-1, 1)
	 * @param rotate positive is right (-1, 1)
	 */
	fun driveFR(forward: Float, strafe: Float, rotate: Float)
	{
		if(localizer == null) throw RuntimeException("localizer cannot be null when calling driveFR");
		localizer!!.update();
		drive(forward, strafe, rotate, localizer!!.poseEstimate.heading.toFloat());
	}

	/**
	 * field relative drive with external angle
	 * @param forward positive is forward (-1, 1)
	 * @param strafe positive is right (-1, 1)
	 * @param rotate positive is right (-1, 1)
	 * @param heading angle(radians)
	 */
	fun drive(forward: Float, strafe: Float, rotate: Float, heading: Float)
	{
		f = (forward * cos(-heading.toDouble()) - strafe * sin(-heading.toDouble())).toFloat();
		r = (forward * sin(-heading.toDouble()) + strafe * cos(-heading.toDouble())).toFloat();
		drive(f, r, rotate);
	}

	/**
	 * robot relative drive with acceleration
	 * @param forward positive is forward (-1, 1)
	 * @param strafe positive is right (-1, 1)
	 * @param rotate positive is right (-1, 1)
	 */
	fun driveAccel(forward: Float, strafe: Float, rotate: Float)
	{
		val out = acceleration.getNext(Vec2(forward, strafe));
		drive(out.x, out.y, rotate);
	}

	/**
	 * field relative drive with acceleration
	 * @param forward positive is forward (-1, 1)
	 * @param strafe positive is right (-1, 1)
	 * @param rotate positive is right (-1, 1)
	 */
	fun driveAccelFR(forward: Float, strafe: Float, rotate: Float)
	{
		val out = acceleration.getNext(Vec2(forward, strafe));
		driveFR(out.x, out.y, rotate);
	}

	/**
	 * robot relative drive
	 * @param forward positive is forward (-1, 1)
	 * @param strafe positive is right (-1, 1)
	 * @param rotate positive is right (-1, 1)
	 */
	fun drive(forward: Float, strafe: Float, rotate: Float)
	{
		var flPwr = forward - strafe - rotate;
		var frPwr = forward + strafe + rotate;
		var blPwr = forward + strafe - rotate;
		var brPwr = forward - strafe + rotate;

		val motorPowers = FloatArray(4);
		motorPowers[0] = abs(flPwr);
		motorPowers[1] = abs(frPwr);
		motorPowers[2] = abs(blPwr);
		motorPowers[3] = abs(brPwr);

		val intendedMaxPower = motorPowers.max();
		if(intendedMaxPower >= maxPower && intendedMaxPower != 0.0f)
		{
			val powerCorrectionRatio = maxPower.toFloat() / intendedMaxPower;
			flPwr *= powerCorrectionRatio;
			frPwr *= powerCorrectionRatio;
			blPwr *= powerCorrectionRatio;
			brPwr *= powerCorrectionRatio;
		}
		frontLeft.power = flPwr.toDouble();
		frontRight.power = frPwr.toDouble();
		backLeft.power = blPwr.toDouble();
		backRight.power = brPwr.toDouble();
		f = forward;
		r = strafe;
	}

	fun telem(t: Telemetry)
	{
		if(localizer != null)
		{
			var ang = Math.toDegrees(localizer!!.poseEstimate.heading).toFloat();
			if(ang > 180)
			{
				ang -= 360f;
			}
			t.addData("(drive)localizerRad", localizer!!.poseEstimate.heading);
			t.addData("(drive)localizerDeg", ang);
		}
		t.addData("(drive)forward", f);
		t.addData("(drive)strafe", r);
		t.addData("(drive)fl", frontLeft.currentPosition);
		t.addData("(drive)fr", frontRight.currentPosition);
		t.addData("(drive)bl", backLeft.currentPosition);
		t.addData("(drive)br", backRight.currentPosition);
	}

	fun debugTelemetry(t: Telemetry)
	{
		val flEx = frontLeft as DcMotorEx;
		val frEx = frontLeft as DcMotorEx;
		val blEx = frontLeft as DcMotorEx;
		val brEx = frontLeft as DcMotorEx;

		t.addData("(current)fl ", flEx.getCurrent(CurrentUnit.MILLIAMPS));
		t.addData("(current)fr ", frEx.getCurrent(CurrentUnit.MILLIAMPS));
		t.addData("(current)bl ", blEx.getCurrent(CurrentUnit.MILLIAMPS));
		t.addData("(current)br ", brEx.getCurrent(CurrentUnit.MILLIAMPS));
		t.addData(
			"total (current)",
			flEx.getCurrent(CurrentUnit.MILLIAMPS) + frEx.getCurrent(CurrentUnit.MILLIAMPS) + blEx.getCurrent(
				CurrentUnit.MILLIAMPS
			) + brEx.getCurrent(CurrentUnit.MILLIAMPS)
		);
		t.addData("(drive)velocity fl", flEx.velocity);
		t.addData("(drive)velocity fr", frEx.velocity);
		t.addData("(drive)velocity bl", blEx.velocity);
		t.addData("(drive)velocity br", brEx.velocity);
		t.addData("(drive)power fl", frontLeft.power);
		t.addData("(drive)power fr", frontRight.power);
		t.addData("(drive)power bl", backLeft.power);
		t.addData("(drive)power br", backRight.power);
	}

	companion object
	{
		@JvmField
		var maxPower = 0.7;
	}
}