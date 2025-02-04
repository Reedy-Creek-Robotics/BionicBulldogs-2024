package org.firstinspires.ftc.teamcode.opmode.config

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU

class HDriveConfig(private val hmap: HardwareMap)
{
	private var frontLeft = "frontLeft"
	private var frontRight = "frontRight"
	private var backLeft = "backLeft"
	private var backRight = "backRight"
	
	fun getFrontLeft(): DcMotor
	{
		return hmap.dcMotor[frontLeft]
	}
	
	fun getFrontRight(): DcMotor
	{
		return hmap.dcMotor[frontRight]
	}
	
	fun getBackLeft(): DcMotor
	{
		return hmap.dcMotor[backLeft]
	}
	
	fun getBackRight(): DcMotor
	{
		return hmap.dcMotor[backRight]
	}
	
	val imu: IMU
		get()
		= hmap.get(IMU::class.java, "imu")
}