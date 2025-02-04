local action = {};

addOpmode({
	name = "other",
	init = function ()
		local builder = sequentalAction();
		local builder2 = parallelAction();

		--local trajBuilder = trajectoryAction(0, 0, 0);
		--trajBuilder:lineToX(24);
		--trajBuilder:setTangent(90);
		--trajBuilder:lineToY(24);
		--trajBuilder:setTangent(0);
		--trajBuilder:lineToX(0);
		--trajBuilder:setTangent(90);
		--trajBuilder:lineToY(0);

		--builder:add(trajBuilder:build());

		--builder2:add(trajBuilder:build());
		--builder2:add(trajBuilder:build());

		--builder:add(builder2:build());
		--builder:add(trajBuilder:build());

		builder:add(sleepAction(4));

		builder2:add(sleepAction(4));
		builder2:add(sleepAction(2));

		builder:add(builder2:build());
		builder:add(sleepAction(4));

		action = builder:build();
	end,
	start = function ()
		runTimer(action);
	end
});