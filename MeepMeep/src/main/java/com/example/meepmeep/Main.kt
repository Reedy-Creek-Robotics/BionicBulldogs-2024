package com.example.meepmeep

import com.acmerobotics.roadrunner.SequentialAction
import com.minerkid08.dynamicopmodeloader.OpmodeLoader
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

fun printActionTree(action: SequentialAction, level: Int = 0, lines: Int = 0): String
{
	var out = "";
	if(level == 0) out += action.javaClass.simpleName + '\n';

	for((i, a) in action.initialActions.withIndex())
	{
		for(i2 in 0 until level)
		{
			out += if((lines and (1 shl i2)) > 0) "│ ";
			else "  ";
		}
		out += if(i == action.initialActions.size - 1) "└─";
		else "├─";

		out += a.javaClass.simpleName + '\n';

		if(a is SequentialAction)
		{
			var lines2 = lines;
			if(i != action.initialActions.size - 1)
				lines2 = lines2 or (1 shl level);
			out += printActionTree(a, level + 1, lines2);
		}
	}
	return out;
}

fun main()
{
	val meepMeep = MeepMeep(800);

	val myBot: RoadRunnerBotEntity = DefaultBotBuilder(meepMeep).setConstraints(
		60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0
	)
		.build();

	drive = myBot.drive;

	val e = OpmodeLoader();
	e.init();
	LuaAction.init(e.getFunctionBuilder());
	e.loadOpmode("testOpmode");
	e.start();

	println(printActionTree(action!! as SequentialAction));

	myBot.runAction(action!!);

	meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
		.setDarkMode(true)
		.setBackgroundAlpha(0.95f)
		.addEntity(myBot)
		.start();
}