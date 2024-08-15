/* Copyright (c) 2023 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.modules.robot;

import android.util.Log;
import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.modules.drive.HDrive;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * This OpMode illustrates using a camera to locate and drive towards a specific AprilTag.
 * The code assumes a Holonomic (Mecanum or X Drive) Robot.
 *
 * For an introduction to AprilTags, see the ftc-docs link below:
 * https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html
 *
 * When an AprilTag in the TagLibrary is detected, the SDK provides location and orientation of the tag, relative to the camera.
 * This information is provided in the "ftcPose" member of the returned "detection", and is explained in the ftc-docs page linked below.
 * https://ftc-docs.firstinspires.org/apriltag-detection-values
 *
 * The drive goal is to rotate to keep the Tag centered in the camera, while strafing to be directly in front of the tag, and
 * driving towards the tag to achieve the desired distance.
 * To reduce any motion blur (which will interrupt the detection process) the Camera exposure is reduced to a very low value (5mS)
 * You can determine the best Exposure and Gain values by using the ConceptAprilTagOptimizeExposure OpMode in this Samples folder.
 *
 * The code assumes a Robot Configuration with motors named: leftfront_drive and rightfront_drive, leftback_drive and rightback_drive.
 * The motor directions must be set so a positive power goes forward on all wheels.
 * This sample assumes that the current game AprilTag Library (usually for the current season) is being loaded by default,
 * so you should choose to approach a valid tag ID (usually starting at 0)
 *
 * Under manual control, the left stick will move forward/back & left/right.  The right stick will rotate the robot.
 * Manually drive the robot until it displays Target data on the Driver Station.
 *
 * Press and hold the *Left Bumper* to enable the automatic "Drive to target" mode.
 * Release the Left Bumper to return to manual driving mode.
 *
 * Under "Drive To Target" mode, the robot has three goals:
 * 1) Turn the robot to always keep the Tag centered on the camera frame. (Use the Target Bearing to turn the robot.)
 * 2) Strafe the robot towards the centerline of the Tag, so it approaches directly in front  of the tag.  (Use the Target Yaw to strafe the robot)
 * 3) Drive towards the Tag to get to the desired distance.  (Use Tag Range to drive the robot forward/backward)
 *
 * Use DESIRED_DISTANCE to set how close you want the robot to get to the target.
 * Speed and Turn sensitivity can be adjusted using the SPEED_GAIN, STRAFE_GAIN and TURN_GAIN constants.
 *
 * Use Android Studio to Copy this Class, and Paste it into the TeamCode/src/main/java/org/firstinspires/ftc/teamcode folder.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 *
 */
@Config
public class DriveToAprilTag
{
    // Adjust these numbers to suit your robot.
    public static double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)

    public static double MIN_SPEED = 0.02f;  // minimum speed of the robot, if the robot goes below this speed then the robot stops tracking the april tag

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    public static double SPEED_GAIN  =  0.50;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    public static double STRAFE_GAIN =  0.25;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    public static double TURN_GAIN   =  0.0;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    public static double MAX_AUTO_SPEED = 0.4;   //  Clip the approach speed to this max value (adjust for your robot)
    public static double MAX_AUTO_STRAFE= 0.4;   //  Clip the approach speed to this max value (adjust for your robot)
    public static double MAX_AUTO_TURN  = 0.2;   //  Clip the turn speed to this max value (adjust for your robot)
    HDrive drive;
    Telemetry telemetry;
    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
    boolean targetFound = false;
    boolean reversed;

    public DriveToAprilTag(HDrive _drive, LinearOpMode op){
        this(_drive, op, "Webcam 1", false);
    }
    public DriveToAprilTag(HDrive _drive, LinearOpMode op, String name, boolean _reversed) {
        drive = _drive;
        telemetry = op.telemetry;
        // Initialize the Apriltag Detection process
        initAprilTag(op, name);
        reversed = _reversed;

        if (USE_WEBCAM)
            setManualExposure(1, 255, op);  // Use low exposure time to reduce motion blur
    }
    public boolean driveToTag(int desiredTagId) {
        double forward = 0;
        double strafe = 0;
        double rotate = 0;
        // Step through the list of detected tags and look for a matching tag
        targetFound = false;    // Set to true when an AprilTag target is detected
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            // Look to see if we have size info on this tag.
            if (detection.metadata != null) {
                //  Check to see if we want to track towards this tag.
                if ((desiredTagId < 0) || (detection.id == desiredTagId)) {
                    // Yes, we want to use this tag.
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                }
            }
        }
        if(desiredTag == null){
            drive.drive(0,0,0);
            telemetry.addLine("Lost tag");
            return false;
        }
        // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
        double rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
        double headingError = desiredTag.ftcPose.bearing;
        double yawError = desiredTag.ftcPose.yaw;

        // Use the speed and turn "gains" to calculate how we want the robot to move.
        forward = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
        rotate = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
        strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

        // Apply desired axes motions to the drivetrain.
        if(
                (Math.abs(forward) > MIN_SPEED || Math.abs(strafe) > MIN_SPEED || Math.abs(rotate) > MIN_SPEED) &&
                        targetFound
        ){
            telemetry.addData("2x", -forward);
            telemetry.addData("2y", -strafe);
            telemetry.addData("2r", -rotate);
            telemetry.addData("1heading", desiredTag.ftcPose.range);
            telemetry.addData("1yaw", desiredTag.ftcPose.bearing);
            telemetry.addData("1range", desiredTag.ftcPose.yaw);
            drive.drive((float)-forward, (float)-strafe, (float)rotate);
            return true;
        }else{
            telemetry.addLine("Made it to tag");
            drive.drive(0,0,0);
            return false;
        }
    }
    public boolean roadRunnerDriveToTag(int id, SampleMecanumDrive rrDrive, Vector2d offset){
        return roadRunnerDriveToTag(id, rrDrive, offset, new Vector2d());
    }
    public boolean roadRunnerDriveToTag(int id, SampleMecanumDrive rrDrive, Vector2d offset, Vector2d multipliers){
        Vector2d targetPos;
        // Step through the list of detected tags and look for a matching tag
        targetFound = false;    // Set to true when an AprilTag target is detected
        List<Vector2d> aprilTagPositions = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            AprilTagDetection tag = null;
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if (id == detection.id) {
                        // Yes, we want to use this tag.
                        targetFound = true;
                        tag = detection;
                        break;  // don't look any further.
                    }
                }
            }
            if (tag != null) {
                Vector2d pos = new Vector2d(tag.ftcPose.y, -tag.ftcPose.x);
                aprilTagPositions.add(pos);
            }
        }

        if(aprilTagPositions.size() == 0){
            return false;
        }

        double avgX = 0.0;
        double avgY = 0.0;
        for(Vector2d pos : aprilTagPositions){
            avgX += pos.getX();
            avgY += pos.getY();
        }
        avgX /= (double)aprilTagPositions.size();
        avgY /= (double)aprilTagPositions.size();
        avgX *= multipliers.getX();
        avgY *= multipliers.getY();

        targetPos = new Vector2d(
                avgX + rrDrive.getPoseEstimate().getX() + offset.getX(),
                 offset.getY()
        );
        Vector2d startPos = rrDrive.getPoseEstimate().vec();
        Log.d("AprilTag" , "Drive To Tag: Estimate pos" + startPos);
        Log.d("AprilTag" , "Tag X" + avgX + ", " + avgY);


        rrDrive.followTrajectorySequence(
                rrDrive.trajectorySequenceBuilder(rrDrive.getPoseEstimate())
                        .lineToConstantHeading(targetPos)
                        .build()
        );

        Vector2d actualPosition = rrDrive.getPoseEstimate().vec();
        Vector2d error = new Vector2d(targetPos.getX() - actualPosition.getX(), targetPos.getY() - actualPosition.getY());
        Log.d("AprilTag",
                "Drive To Tag: Expected pos: " +
                        targetPos.getX() + ", " + targetPos.getY() +
                        " | actual position: " +
                        actualPosition.getX() + ", " + actualPosition.getY() +
                        " | error: " +
                        error.getX() + ", " + error.getY()
                        );
        return true;
    }
    public void telemetry() {
        if(desiredTag != null) {
            telemetry.addData("found tag", desiredTag.id);
        }
        telemetry.addData("was tag found", targetFound);
    }
    public void initTelem(){
        int i = 0;
        for(AprilTagDetection detection : aprilTag.getDetections()){
            telemetry.addData("tag" + (++i), detection.id);
            telemetry.addData("tag" + (i) + " position",  detection.ftcPose.y + ", " + -detection.ftcPose.x);
            telemetry.addData("tag" + (i) + " target position: ", (detection.ftcPose.y + 4) + ", " + detection.ftcPose.x);

        }
    }
    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag(LinearOpMode op, String name) {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        CameraName camera = op.hardwareMap.get(WebcamName.class, name);
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(camera)
                    .setCameraResolution(new Size(640, 480))
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    void delay(float ms){
        ElapsedTime elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        while(elapsedTime.milliseconds() < ms);
    }

    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void    setManualExposure(int exposureMS, int gain, LinearOpMode op) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!op.isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING));

            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (!op.isStopRequested())
        {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                delay(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            delay(20);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            delay(20);
        }
    }
}