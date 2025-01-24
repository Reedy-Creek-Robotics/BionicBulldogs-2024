package org.firstinspires.ftc.teamcode.opmode.telop

import android.content.Context
import android.util.Log
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.minerkid08.dynamicopmodeloader.*
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.ftccommon.external.OnCreate
import org.firstinspires.ftc.teamcode.modules.actions.drive
import org.firstinspires.ftc.teamcode.modules.actions.initComponents

class LuaTrajectoryBuilder(private val builder: TrajectoryActionBuilder)
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.classAddFun(
				LuaTrajectoryBuilder::class.java,
				"setTangent",
				LuaType.Void(),
				listOf(LuaType.Double())
			);
			builder.classAddFun(
				LuaTrajectoryBuilder::class.java,
				"lineToX",
				LuaType.Void(),
				listOf(LuaType.Double())
			);
			builder.classAddFun(
				LuaTrajectoryBuilder::class.java,
				"lineToY",
				LuaType.Void(),
				listOf(LuaType.Double())
			);
			builder.classAddFun(
				LuaTrajectoryBuilder::class.java,
				"splineToLinearHeading",
				LuaType.Void(),
				listOf(LuaType.Double(), LuaType.Double(), LuaType.Double(), LuaType.Double())
			);
			builder.classAddFun(
				LuaTrajectoryBuilder::class.java,
				"splineToConstantHeading",
				LuaType.Void(),
				listOf(LuaType.Double(), LuaType.Double(), LuaType.Double())
			);

			builder.classAddFun(
				LuaTrajectoryBuilder::class.java,
				"build",
				LuaType.Object(Action::class.java)
			);
		}
	}

	fun setTangent(tangent: Double)
	{
		builder.setTangent(tangent);
	}

	fun lineToX(x: Double)
	{
		builder.lineToX(x);
	}

	fun lineToY(y: Double)
	{
		builder.lineToY(y);
	}

	fun splineToLinearHeading(x: Double, y: Double, h: Double, t: Double)
	{
		builder.splineToLinearHeading(Pose2d(x, y, h), t);
	}

	fun splineToConstantHeading(x: Double, y: Double, t: Double)
	{
		builder.splineToConstantHeading(Vector2d(x, y), t);
	}

	fun build(): Action
	{
		return builder.build();
	}
}

class LuaAction
{
	companion object
	{
		fun init(builder: FunctionBuilder)
		{
			builder.setCurrentObject(LuaAction());

			builder.objectAddFun("run", LuaType.Void(), listOf(LuaType.Object(Action::class.java)));
			builder.objectAddFun("print", LuaType.Void(), listOf(LuaType.String()));
			builder.objectAddFun("trajectoryAction", LuaType.Object(LuaTrajectoryBuilder::class.java), listOf(LuaType.Double(), LuaType.Double(), LuaType.Double()));

			builder.createClass("Action");
		}
	}

	fun trajectoryAction(x: Double, y: Double, h: Double): LuaTrajectoryBuilder
	{
		return LuaTrajectoryBuilder(drive.actionBuilder(Pose2d(x, y, h)));
	}

	fun run(action: Action)
	{
		runBlocking(action);
	}

	fun print(str: String)
	{
		Log.d("lua", str);
	}
}

@OnCreate
fun main(c: Context)
{
	FileServer.start();
}

@TeleOp
class OpmodeLoaderTest: LinearOpMode()
{
	override fun runOpMode()
	{
		initComponents(hardwareMap);
		val opmodeloader = OpmodeLoader();
		opmodeloader.init();

		val builder = opmodeloader.getFunctionBuilder();
		LuaTrajectoryBuilder.init(builder);
		LuaAction.init(builder);

		opmodeloader.loadOpmode("testOpmode");

		waitForStart();

		opmodeloader.start();
	}
}