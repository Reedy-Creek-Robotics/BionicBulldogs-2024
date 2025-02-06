require("utils")

---@type Action
local action = {}

addOpmode({
	name = "testOpmode",
	init = function ()
		initExternalFunctions();

		setPosEstimate(7.0, -7.5, 180);
		local builder = sequentalAction();

		-- score specimen 1 (preload)

		builder:add(specimenGrabInstant());
		local trajectory = trajectoryActionX(7.0, -7.5, 180, overides({ minAccel = -40 }));
		trajectory:lineToX(36.25);
		builder:add(trajectory:build());
		builder:add(specimenScore());
		--builder:add(sleepAction(0.1));

		-- grab specimen 2

		trajectory = trajectoryActionX(36, -7.5, 180, overides({ minAccel = -40 }));
		trajectory:setTangent(-180);
		trajectory:splineToLinearHeading(8, -40.5, 0, -180)
		builder:add(trajectory:build());
		builder:add(specimenGrab());

		-- score specimen 2

		trajectory = trajectoryActionX(8.5, -40.5, 0.0, overides({ minAccel = -40 }));
		trajectory:setTangent(45);
		trajectory:splineToLinearHeading(36.25, -3.5, 180, 0);
		builder:add(trajectory:build());
		builder:add(specimenScore());
		builder:add(sleepAction(0.1));

		-- push samples

		trajectory = trajectoryActionX(36, -6.5, 180.0, overides({ vel = 100, minAccel = -65, maxAccel = 65 }));
		trajectory:setTangent(-180);
		trajectory:splineToLinearHeading(32.0, -36.0, 0, -90.0);
		trajectory:setTangent(0.0);
		trajectory:splineToConstantHeading(55.0, -47.0, -90.0);
		trajectory:setTangent(180.0);
		trajectory:lineToX(17.0);
		trajectory:setTangent(0.0);
		trajectory:splineToConstantHeading(56.0, -55.0, -90.0);
		trajectory:setTangent(180.0);
		trajectory:lineToX(17.0);
		trajectory:setTangent(0.0);
		trajectory:splineToConstantHeading(56.0, -62.5, -90.0);
		trajectory:setTangent(180.0);
		--trajectory:lineToX(17.0);
		trajectory:lineToX(7.75);
		builder:add(trajectory:build());

		-- grab specimen 3

		--trajectory = trajectoryActionX(17.0, -62.5, 0.0, overides({minAccel = -40}));
		--trajectory:setTangent(90.0);
		--trajectory:splineToConstantHeading(7.75, -40.5, -180.0);
		--builder:add(trajectory:build());
		builder:add(specimenGrab());

		-- score specimen 3

		trajectory = trajectoryActionX(8.5, -40.5, 0.0, overides({minAccel = -40}));
		trajectory:setTangent(45.0);
		trajectory:splineToLinearHeading(36.25, -1.5, 180.0, 0);
		builder:add(trajectory:build());
		builder:add(sleepAction(0.025));
		builder:add(specimenScore());

		-- grab specimen 4

		builder:add(sleepAction(0.05));
		trajectory = trajectoryActionX(35.0, -1.5, 180.0, overides({minAccel = -40}));
		trajectory:setTangent(-180.0);
		trajectory:splineToLinearHeading(7.75, -40.5, 0.0, -180.0);
		builder:add(trajectory:build());
		builder:add(specimenGrab());

		-- score specimen 4

		trajectory = trajectoryActionX(8.5, -40.5, 0.0, overides({minAccel = -40}));
		trajectory:setTangent(45.0);
		trajectory:splineToLinearHeading(36.0, 1.5, 180.0, 0);
		builder:add(trajectory:build());
		builder:add(sleepAction(0.05));
		builder:add(specimenScore());

		-- grab specimen 5

		builder:add(sleepAction(0.05));
		trajectory = trajectoryActionX(35.0, -1.5, 180.0, overides({minAccel = -40}));
		trajectory:setTangent(-180.0);
		trajectory:splineToLinearHeading(7.75, -40.5, 0.0, -180.0);
		builder:add(trajectory:build());
		builder:add(specimenGrab());

		-- score specimen 5

		trajectory = trajectoryActionX(8.5, -40.5, 0.0, overides({minAccel = -40}));
		trajectory:setTangent(45.0);
		trajectory:splineToLinearHeading(36.0, 4, 180.0, 0.0);
		builder:add(trajectory:build());
		builder:add(sleepAction(0.05));
		builder:add(specimenScore());

		builder:add(sleepAction(0.2));
		action = builder:build();
	end,
	start = function ()
		runTimer(action, "specimenAuto2.txt");
	end
})

addOpmode({
	name = "builderTest",
	init = function()
		action = trajectoryAction(0, 0, 0)
			:splineToConstantHeading(0, 12, 0)
			:splineToConstantHeading(12, 12, 0)
			:splineToConstantHeading(12, 0, 0)
			:splineToConstantHeading(0, 0, 0)
			:build();
	end,
	start = function()
		run(action);
	end
})