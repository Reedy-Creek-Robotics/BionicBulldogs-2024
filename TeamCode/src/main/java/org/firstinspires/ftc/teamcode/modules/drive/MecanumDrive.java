package org.firstinspires.ftc.teamcode.modules.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrive {
    DcMotorEx frontLeft;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx backRight;

    Telemetry telemetry;

    public MecanumDrive(
        DcMotorEx frontLeft,
        DcMotorEx frontRight,
        DcMotorEx backLeft,
        DcMotorEx backRight,
        Telemetry telemetry
    ) {
        this.telemetry = telemetry;

        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        DcMotorEx[] motors = new DcMotorEx[] {
            frontLeft, frontRight, backLeft, backRight
        };
        for( DcMotorEx m : motors ) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void drive(double forward, double strafe, double turn) {
        drive(forward, strafe, turn, 0.0);
        updateTelemetry();
    }

    public void drive(double forward, double strafe, double turn, double heading) {
        // forward is the y direction and strafe is the x direction on coordinate grid
        double rotX = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
        double rotY = strafe * Math.sin(-heading) + forward * Math.cos(-heading);

        // TODO: normalize the wheel powers in case these go over 1 (find max and divide them all by it)
        frontLeft.setPower(rotY + rotX + turn);
        backLeft.setPower(rotY - rotX + turn);
        frontRight.setPower(rotY - rotX - turn);
        backRight.setPower(rotY + rotX - turn);

        updateTelemetry();
    }

    protected void updateTelemetry() {
        telemetry.addData("frontLeft:", frontLeft.getPower());
        telemetry.addData("frontRight:", frontRight.getPower());
        telemetry.addData("backLeft:", backLeft.getPower());
        telemetry.addData("backRight:", backRight.getPower());
    }
}
