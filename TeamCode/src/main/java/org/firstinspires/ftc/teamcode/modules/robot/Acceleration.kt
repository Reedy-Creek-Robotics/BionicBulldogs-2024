package org.firstinspires.ftc.teamcode.modules.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.modules.Vec2
import org.firstinspires.ftc.teamcode.modules.lerp

@Config
class Acceleration
{
		private var elapsedTime: ElapsedTime = ElapsedTime();
		private var prevTime = 0f;
		private var prevOut: Vec2 = Vec2();

		init
		{
				elapsedTime.reset();
		}

		fun resetTime()
		{
				elapsedTime.reset();
				prevOut.x = 0f;
				prevOut.y = 0f;
				prevTime = 0f;
		}

		fun getNext(inVal: Vec2): Vec2
		{
				val currentTime = elapsedTime.seconds().toFloat();
				val deltaTime = currentTime - prevTime;
				prevTime = currentTime;
				prevOut.x = lerp(prevOut.x, inVal.x, deltaTime / accelTime.toFloat());
				prevOut.y = lerp(prevOut.y, inVal.y, deltaTime / accelTime.toFloat());
				return prevOut;
		}

		companion object
		{
				@JvmField
				var accelTime = 0.25
		}
}