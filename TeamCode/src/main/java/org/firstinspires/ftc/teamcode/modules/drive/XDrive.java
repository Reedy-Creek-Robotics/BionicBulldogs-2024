package org.firstinspires.ftc.teamcode.modules.drive;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.modules.robot.Acceleration;
import org.firstinspires.ftc.teamcode.opmode.config.XDriveConfig;
import org.firstinspires.ftc.teamcode.modules.hardware.ImuEx;
import org.firstinspires.ftc.teamcode.modules.hardware.MotorGroup;
import org.firstinspires.ftc.teamcode.roadrunner.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.modules.Vec2;

import java.util.ArrayList;
import java.util.List;

@Config
public class XDrive {
    public static double maxPower = 1.0;
    public MotorGroup motors;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    float f;
    float r;
    ImuEx imu;
    StandardTrackingWheelLocalizer localizer;
    List<Integer> trackingEncoderPositions;
    List<Integer> trackingEncoderVelocities;
    Acceleration acceleration;

    public void debugTelemetry(Telemetry t) {
        t.addData("(current)fl ", ((DcMotorEx)frontLeft).getCurrent(CurrentUnit.MILLIAMPS));
        t.addData("(current)fr", ((DcMotorEx)frontRight).getCurrent(CurrentUnit.MILLIAMPS));
        t.addData("(current)bl", ((DcMotorEx)backLeft).getCurrent(CurrentUnit.MILLIAMPS));
        t.addData("(current)br", ((DcMotorEx)backRight).getCurrent(CurrentUnit.MILLIAMPS));

        t.addData("total (current)",
                ((DcMotorEx)frontLeft).getCurrent(CurrentUnit.MILLIAMPS) +
                ((DcMotorEx)frontRight).getCurrent(CurrentUnit.MILLIAMPS) +
                ((DcMotorEx)backLeft).getCurrent(CurrentUnit.MILLIAMPS) +
                ((DcMotorEx)backRight).getCurrent(CurrentUnit.MILLIAMPS)
        );

        double totalCurrent = ((DcMotorEx)frontLeft).getCurrent(CurrentUnit.MILLIAMPS) +
                ((DcMotorEx)frontRight).getCurrent(CurrentUnit.MILLIAMPS) +
                ((DcMotorEx)backLeft).getCurrent(CurrentUnit.MILLIAMPS) +
                ((DcMotorEx)backRight).getCurrent(CurrentUnit.MILLIAMPS);
        if( totalCurrent > 0 ) {
            Log.d("BIONIC", "Total Current = " + totalCurrent);
        }

        t.addData("(drive)velocity fl", ((DcMotorEx)frontLeft).getVelocity());
        t.addData("(drive)velocity fr", ((DcMotorEx)frontRight).getVelocity());
        t.addData("(drive)velocity bl", ((DcMotorEx)backLeft).getVelocity());
        t.addData("(drive)velocity br", ((DcMotorEx)backRight).getVelocity());

        t.addData("(drive)power fl", frontLeft.getPower());
        t.addData("(drive)power fr", frontRight.getPower());
        t.addData("(drive)power bl", backLeft.getPower());
        t.addData("(drive)power br", backRight.getPower());
    }

    public XDrive(){

    }
    public XDrive(XDriveConfig cfg){
        init(cfg);
    }

    public void init(XDriveConfig config){
        trackingEncoderPositions = new ArrayList<Integer>();
        trackingEncoderVelocities = new ArrayList<Integer>();
        localizer = new StandardTrackingWheelLocalizer(config.getHmap(), trackingEncoderPositions, trackingEncoderVelocities);
        frontLeft = config.getFrontLeft();
        frontRight = config.getFrontRight();
        backLeft = config.getBackLeft();
        backRight = config.getBackRight();

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        motors = new MotorGroup();
        motors.addMotor(frontLeft);
        motors.addMotor(frontRight);
        motors.addMotor(backLeft);
        motors.addMotor(backRight);

        motors.resetEncoders();
        motors.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motors.setZeroPower(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = new ImuEx(config.getImu());
        acceleration = new Acceleration();
    }
    public void setPosEstimate(Pose2d pos){
        localizer.setPoseEstimate(pos);
    }

    public void start(){
        acceleration.resetTime();
    }

    /**
     * Drives robot with current heading
     * @param forward
     * @param strafe
     * @param rotate
     */
    public void driveCH(float forward, float strafe, float rotate){
        localizer.update();
        drive(forward, strafe, rotate, (float)localizer.getPoseEstimate().getHeading());
    }

    public void drive(float forward, float strafe, float rotate, float heading){
        f = (float)(forward * Math.cos(heading) - strafe * Math.sin(heading));
        r = (float)(forward * Math.sin(heading) + strafe * Math.cos(heading));
        drive(f, r, rotate);
    }

    public void driveAccel(float forward, float strafe, float rotate){
        Vec2 out = acceleration.getNext(new Vec2(forward, strafe));
        forward = out.x;
        strafe = out.y;
        drive(forward, strafe, rotate, 0);
    }
    public void driveAccelFR(float forward, float strafe, float rotate){
        Vec2 out = acceleration.getNext(new Vec2(forward, strafe));
        forward = out.x;
        strafe = out.y;
        driveCH(forward, strafe, rotate);
    }
    public void drive(float forward, float strafe, float rotate){
        float flPwr = forward + strafe - rotate;
        float frPwr = forward - strafe + rotate;
        float blPwr = forward - strafe - rotate;
        float brPwr = forward + strafe + rotate;
        
        // determine if any motor was going to go over max power and if so scale it back to 1.0
        // and all other motors should scale by the same amount
        float intendedMaxPower = Math.max(Math.abs(flPwr), Math.max(Math.abs(frPwr), Math.max(Math.abs(blPwr), Math.abs(brPwr))));
        if(intendedMaxPower >= maxPower && intendedMaxPower != 0.0) {
            float powerCorrectionRatio = (float)maxPower / intendedMaxPower;

            flPwr *= powerCorrectionRatio;
            frPwr *= powerCorrectionRatio;
            blPwr *= powerCorrectionRatio;
            brPwr *= powerCorrectionRatio;
        }
        else
        {
            flPwr = Math.max(Math.min(flPwr, (float)maxPower), -(float)maxPower);
            frPwr = Math.max(Math.min(frPwr, (float)maxPower), -(float)maxPower);
            blPwr = Math.max(Math.min(blPwr, (float)maxPower), -(float)maxPower);
            brPwr = Math.max(Math.min(brPwr, (float)maxPower), -(float)maxPower);
        }

        frontLeft.setPower(flPwr);
        frontRight.setPower(frPwr);
        backLeft.setPower(blPwr);
        backRight.setPower(brPwr);
        f = forward;
        r = strafe;
    }

    public void telem(Telemetry t){
        float ang = (float)Math.toDegrees(localizer.getPoseEstimate().getHeading());
        if(ang > 180){
            ang -= 360;
        }
        t.addData("(drive)localizerRad", localizer.getPoseEstimate().getHeading());
        t.addData("(drive)localizerDeg", ang);
        t.addData("(drive)imuPitch", imu.getImu().getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES));
        t.addData("(drive)imuYaw", imu.getImu().getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        t.addData("(drive)imuRoll", imu.getImu().getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES));
        t.addData("(drive)imuOrientationX", imu.getImu().getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle);
        t.addData("(drive)imuOrientationY", imu.getImu().getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle);
        t.addData("(drive)imuOrientationZ", imu.getImu().getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);
        t.addData("(drive)forward", f);
        t.addData("(drive)strafe", r);
        t.addData("(drive)fl", frontLeft.getCurrentPosition());
        t.addData("(drive)fr", frontRight.getCurrentPosition());
        t.addData("(drive)bl", backLeft.getCurrentPosition());
        t.addData("(drive)br", backRight.getCurrentPosition());
    }
}
