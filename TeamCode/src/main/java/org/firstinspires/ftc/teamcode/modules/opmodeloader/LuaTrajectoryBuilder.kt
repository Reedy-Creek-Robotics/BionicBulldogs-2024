package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType

class LuaTrajectoryBuilder(private var builder: TrajectoryActionBuilder)
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"setTangent",
				LuaType.Void,
				listOf(LuaType.Double)
			);
			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"lineToX",
				LuaType.Void,
				listOf(LuaType.Double)
			);
			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"lineToY",
				LuaType.Void,
				listOf(LuaType.Double)
			);
			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineToLinearHeading",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);
			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineToConstantHeading",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double)
			);
			
			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"build",
				LuaType.Object(Action::class.java)
			);
		}
	}
	
	fun setTangent(tangent: Double)
	{
		builder = builder.setTangent(Math.toRadians(tangent));
	}
	
	fun lineToX(x: Double)
	{
		builder = builder.lineToX(x);
	}
	
	fun lineToY(y: Double)
	{
		builder = builder.lineToY(y);
	}
	
	fun splineToLinearHeading(x: Double, y: Double, h: Double, t: Double)
	{
		builder = builder.splineToLinearHeading(Pose2d(x, y, Math.toRadians(h)), Math.toRadians(t));
	}
	
	fun splineToConstantHeading(x: Double, y: Double, t: Double)
	{
		builder = builder.splineToConstantHeading(Vector2d(x, y), Math.toRadians(t));
	}
	
	fun build(): Action
	{
		return builder.build();
	}
}