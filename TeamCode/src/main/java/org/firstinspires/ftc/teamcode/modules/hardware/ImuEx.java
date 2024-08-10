package org.firstinspires.ftc.teamcode.modules.hardware;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ImuEx {
    IMU imu;

    public ImuEx(IMU _imu, RevHubOrientationOnRobot.UsbFacingDirection usb, RevHubOrientationOnRobot.LogoFacingDirection logo) {
        imu = _imu;
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(logo, usb)));
        imu.resetYaw();
    }

    public ImuEx(IMU _imu) {
        imu = _imu;
        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,// Right, Back for control hub : Left, Forward for expansion hub : Forward, Up for test bot
                                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                        )
                )
        );
        imu.resetYaw();
    }

    public void resetHeading() {
        imu.resetYaw();
    }

    public float getHeading(AngleUnit unit) {
        return (float)imu.getRobotYawPitchRollAngles().getYaw(unit);
        //return imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, unit).secondAngle;
    }

    public IMU getImu(){
        return imu;
    }
}
