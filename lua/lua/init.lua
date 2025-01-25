local action = {}

addOpmode({
	name = "testOpmode",
	init = function ()
		local builder = sequentalAction();

		builder:add(specimenGrab());

		local trajectory = trajectoryAction(7, -7.5, 180);
		trajectory:lineToX(36);
		builder:add(trajectory:build());

		builder:add(specimenScore());
		builder:add(sleepAction(1.0));

		action = builder:build();
	end,
	start = function ()
		run(action);
	end
})