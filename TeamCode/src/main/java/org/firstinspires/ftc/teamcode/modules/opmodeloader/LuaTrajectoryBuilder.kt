package org.firstinspires.ftc.teamcode.modules.opmodeloader

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import com.minerkid08.dynamicopmodeloader.FunctionBuilder
import com.minerkid08.dynamicopmodeloader.LuaType
import org.firstinspires.ftc.teamcode.modules.actions.drive
import org.firstinspires.ftc.teamcode.opmode.auto.accelOverrideRaw
import org.firstinspires.ftc.teamcode.opmode.auto.velOverrideRaw

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
				"lineToX2",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"lineToY",
				LuaType.Void,
				listOf(LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"lineToY2",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineToLinearHeading",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineToLinearHeading2",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineToConstantHeading",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineToConstantHeading2",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineTo",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"splineTo2",
				LuaType.Void,
				listOf(LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double, LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"turnTo",
				LuaType.Void,
				listOf(LuaType.Double)
			);

			builder.addClassFunction(
				LuaTrajectoryBuilder::class.java,
				"build",
				LuaType.Object(Action::class.java)
			);

			builder.addClassFunction(LuaTrajectoryBuilder::class.java, "getEndX", LuaType.Double);
			builder.addClassFunction(LuaTrajectoryBuilder::class.java, "getEndY", LuaType.Double);
			builder.addClassFunction(LuaTrajectoryBuilder::class.java, "getEndH", LuaType.Double);
		}
	}

	private var intEndX = 0.0;
	private var intEndY = 0.0;
	private var intEndH = 0.0;

	constructor(x: Double, y: Double, h: Double):
		this(drive.actionBuilder(Pose2d(x, y, Math.toRadians(h))))
	{
		intEndX = x;
		intEndY = y;
		intEndH = h;
	}

	constructor(x: Double, y: Double, h: Double, vel: Double, minAccel: Double, maxAccel: Double):
		this(
			drive.actionBuilder(
				Pose2d(x, y, Math.toRadians(h)),
				velOverrideRaw(vel),
				accelOverrideRaw(minAccel, maxAccel)
			)
		)
	{
		intEndX = x;
		intEndY = y;
		intEndH = h;
	}

	fun setTangent(tangent: Double)
	{
		builder = builder.setTangent(Math.toRadians(tangent));
	}

	fun lineToX(x: Double)
	{
		intEndX = x;
		builder = builder.lineToX(x);
	}

	fun lineToX2(x: Double, vel: Double, minAccel: Double, maxAccel: Double)
	{
		intEndX = x;
		builder = builder.lineToX(x, velOverrideRaw(vel), accelOverrideRaw(minAccel, maxAccel));
	}

	fun lineToY(y: Double)
	{
		intEndY = y;
		builder = builder.lineToY(y);
	}

	fun lineToY2(x: Double, vel: Double, minAccel: Double, maxAccel: Double)
	{
		intEndY = x;
		builder = builder.lineToY(x, velOverrideRaw(vel), accelOverrideRaw(minAccel, maxAccel));
	}

	fun splineToLinearHeading(x: Double, y: Double, h: Double, t: Double)
	{
		intEndX = x;
		intEndY = y;
		intEndH = h;
		builder = builder.splineToLinearHeading(Pose2d(x, y, Math.toRadians(h)), Math.toRadians(t));
	}

	fun splineToLinearHeading2(x: Double, y: Double, h: Double, t: Double, vel: Double, minAccel: Double, maxAccel: Double)
	{
		intEndX = x;
		intEndY = y;
		intEndH = h;
		builder = builder.splineToLinearHeading(Pose2d(x, y, Math.toRadians(h)), Math.toRadians(t), velOverrideRaw(vel), accelOverrideRaw(minAccel, maxAccel));
	}

	fun splineToConstantHeading(x: Double, y: Double, t: Double)
	{
		intEndX = x;
		intEndY = y;
		builder = builder.splineToConstantHeading(Vector2d(x, y), Math.toRadians(t));
	}

	fun splineToConstantHeading2(x: Double, y: Double, t: Double, vel: Double, minAccel: Double, maxAccel: Double)
	{
		intEndX = x;
		intEndY = y;
		builder = builder.splineToConstantHeading(Vector2d(x, y), Math.toRadians(t), velOverrideRaw(vel), accelOverrideRaw(minAccel, maxAccel));
	}

	fun splineTo(x: Double, y: Double, t: Double)
	{
		intEndX = x;
		intEndY = y;
		builder = builder.splineTo(Vector2d(x, y), Math.toRadians(t));
	}

	fun splineTo2(x: Double, y: Double, t: Double, vel: Double, minAccel: Double, maxAccel: Double)
	{
		intEndX = x;
		intEndY = y;
		builder = builder.splineTo(Vector2d(x, y), Math.toRadians(t), velOverrideRaw(vel), accelOverrideRaw(minAccel, maxAccel));
	}

	fun turnTo(h: Double)
	{
		intEndH = h;
		builder = builder.turnTo(Math.toRadians(h));
	}

	fun getEndX(): Double
	{
		return intEndX;
	}

	fun getEndY(): Double
	{
		return intEndY;
	}

	fun getEndH(): Double
	{
		return intEndH;
	}

	fun build(): Action
	{
		return builder.build();
	}
}