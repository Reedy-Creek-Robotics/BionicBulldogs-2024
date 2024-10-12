package org.firstinspires.ftc.teamcode.opmode.telop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.modules.hardware.GamepadEx

@TeleOp
class HardwareTester : OpMode() {
    private lateinit var controller: GamepadEx;
    private var selectedDevice = 0;
    private lateinit var devices: MutableList<HardwareDevice>;
    var targetPosition = 0.0;
    override fun init() {
        devices = ArrayList()
        for (device in hardwareMap) {
            devices.add(device)
        }
        controller = GamepadEx(gamepad1)
    }

    override fun loop() {
        // Output the selected device to telemetry
        val device = devices[selectedDevice]
        telemetry.addData("Device Count", selectedDevice.toString() + " of " + devices!!.size)
        telemetry.addData("Device", hardwareMap.getNamesOf(device))
        telemetry.addData("Type", device.javaClass.simpleName)
        if (controller.rightBumper()) {
            selectedDevice++
            if (selectedDevice >= devices.size) selectedDevice = 0
        } else if (controller.leftBumper()) {
            selectedDevice--
            if (selectedDevice < 0) selectedDevice = devices.size - 1
        }
        if (device is DcMotorEx) {
            val motor = device
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
            telemetry.addData("Position", motor.currentPosition)
            telemetry.addData("Power", motor.power)
            telemetry.addData("encoder tolerance", motor.targetPositionTolerance)
            val power: Double = gamepad1.left_stick_y.toDouble()
            motor.power = power * 0.5
            if (controller.circle()) {
                motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            }

            /*if(controller.isPressed(Controller.Button.SQUARE)){
                motor.setTargetPosition();
            }*/
        } else if (device is Servo) {
            val servo = device
            telemetry.addData("Position", servo.position)
            if (controller.rightTriggerb()) {
                targetPosition += gamepad1.right_stick_y * 0.01
            }
            if (controller.dpadUp()) {
                targetPosition += 0.05
            } else if (controller.dpadDown()) {
                targetPosition -= 0.05
            }
            servo.position = targetPosition
            if (controller.circle()) {
                targetPosition = 0.0
                servo.position = targetPosition
            }
        }
        telemetry.update()
    }
}