require("utils");

local action = {}

addOpmode({
	name = "specimen",
	init = function ()
		setPosEstimate(7, -7.5, 180);
		local actionBuilder = sequentalAction();
		-- grab
		local trajectoryBuilder = trajectoryActionX(7, -7.5, 180, overrides({ minAccel = -20 }));
		trajectoryBuilder:lineToX(36.125);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		actionBuilder:add(sleepAction(0.1));

		trajectoryBuilder = trajectoryAction(36.0, -7.5, 180);
		trajectoryBuilder:setTangent(-180);
		trajectoryBuilder:lineToX(32);
		trajectoryBuilder:setTangent(-135);
		trajectoryBuilder:splineToLinearHeading(14.5, -40.5, 0, -135);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:lineToX(8);
		actionBuilder:add(trajectoryBuilder:build());

		-- grab

		trajectoryBuilder = trajectoryAction(8.5, -40.5, 0.0);
		trajectoryBuilder:setTangent(45);
		trajectoryBuilder:splineToLinearHeading(32.0, -5.5, 180, 45);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:lineToX(36.0);
		actionBuilder:add(trajectoryBuilder:build());

		--score

		actionBuilder:add(sleepAction(0.1));

		trajectoryBuilder = trajectoryActionX(36, -6.5, 180, overrides({ vel = 100, minAccel = -75, maxAccel = 75 }));
		trajectoryBuilder:setTangent(-180);
		trajectoryBuilder:splineToConstantHeading(32, -36, -90);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:splineToConstantHeading(55, -47, -90);
		trajectoryBuilder:setTangent(180);
		trajectoryBuilder:lineToX(17);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:splineToConstantHeading(56, -55, -90);
		trajectoryBuilder:setTangent(180);
		trajectoryBuilder:lineToX(17);
		actionBuilder:add(trajectoryBuilder:build());

		trajectoryBuilder = trajectoryAction(17, -56, 180);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:splineToLinearHeading(14.5, -40.5, 0, -135);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:lineToX(7.75);
		actionBuilder:add(trajectoryBuilder:build());

		-- grab

		trajectoryBuilder = trajectoryAction(8.5, -40.5, 0)
		trajectoryBuilder:setTangent(45);
		trajectoryBuilder:splineToLinearHeading(32, -1.5, 180, 45);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:lineToX(35.5);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		actionBuilder:add(sleepAction(0.1));

		trajectoryBuilder = trajectoryAction(35, -1.5, 180);
		trajectoryBuilder:setTangent(-180);
		trajectoryBuilder:lineToX(32);
		trajectoryBuilder:setTangent(-135);
		trajectoryBuilder:splineToLinearHeading(14.5, -40.5, 0, -135);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:lineToX(7.75);
		actionBuilder:add(trajectoryBuilder:build());

		-- grab

		trajectoryBuilder = trajectoryAction(8.5, -40.5, 0);
		trajectoryBuilder:setTangent(45);
		trajectoryBuilder:splineToLinearHeading(32, 1.5, 180, 45);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:lineToX(35.5);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		actionBuilder:add(sleepAction(0.1));

		-- hslide goto min

		trajectoryBuilder = trajectoryActionX(36, -2.5, 180, overrides({ vel = 100, minAccel = -75, maxAccel = 75 }));
		trajectoryBuilder:setTangent(-135);
		trajectoryBuilder:splineToLinearHeading(22, -30, -135, -135);
		actionBuilder:add(trajectoryBuilder:build());

		action = actionBuilder:build();
	end,
	start = function ()
		run(action);
	end
})