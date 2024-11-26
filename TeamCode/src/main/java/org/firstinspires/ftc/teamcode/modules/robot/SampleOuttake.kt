package org.firstinspires.ftc.teamcode.modules.robot

class SampleOuttake(private val slide: Slide, private val outtake: Outtake)
{
	fun init()
	{
		outtake.armDown();
		outtake.bucketDown();
	}

	fun up()
	{
		slide.gotoPos(-2500);
		outtake.up();
	}

	fun score()
	{
		outtake.score();
	}

	fun update()
	{
		if(outtake.update() == 1)
		{
			slide.lower()
		}
	}

	fun waitUntilIdle()
	{
		while(outtake.state != Outtake.State.Idle)
		{
			update();
		}
	}
}