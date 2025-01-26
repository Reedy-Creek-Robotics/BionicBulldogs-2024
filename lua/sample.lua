require("utils");

local action = {}

addOpmode({
	name = "sample",
	init = function ()
		local actionBuilder = sequentalAction();

		local trajectoryBuilder = trajectoryAction(7.5, 30.5, -90);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:splineToLinearHeading(15.5, 56, -45, 135);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		trajectoryBuilder = trajectoryAction(15.5, 56, -45.0);
		trajectoryBuilder:setTangent(-45);
		trajectoryBuilder:splineToLinearHeading(24.5, 40, 0, 0);
		trajectoryBuilder:setTangent(90);
		trajectoryBuilder:lineToY(46)
		actionBuilder:add(trajectoryBuilder:build());

		-- grab

		trajectoryBuilder = trajectoryAction(25, 45, 0);
		trajectoryBuilder:setTangent(135);
		trajectoryBuilder:splineToLinearHeading(16.5, 55, -45, 135);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		trajectoryBuilder = trajectoryAction(16.5, 55, -45);
		trajectoryBuilder:setTangent(-45);
		trajectoryBuilder:splineToLinearHeading(24.75, 50.5, 0, 0);
		trajectoryBuilder:setTangent(90);
		trajectoryBuilder:lineToY(53.0)
		actionBuilder:add(trajectoryBuilder:build());

		-- grab

		trajectoryBuilder = trajectoryAction(25, 53, 0);
		trajectoryBuilder:setTangent(135);
		trajectoryBuilder:splineToLinearHeading(16.5, 55.5, -45, 135);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		trajectoryBuilder = trajectoryAction(16.5, 55.5, -45);
		trajectoryBuilder:setTangent(-45);
		trajectoryBuilder:splineToLinearHeading(45.75, 49, 90, 0);
		actionBuilder:add(trajectoryBuilder:build());

		-- grab

		trajectoryBuilder = trajectoryAction(46, 49, 90);
		trajectoryBuilder:setTangent(135);
		trajectoryBuilder:splineToLinearHeading(16.5, 55.5, -45, 135);
		actionBuilder:add(trajectoryBuilder:build());

		-- score

		trajectoryBuilder = trajectoryAction(16.5, 55.5, -45);
		trajectoryBuilder:setTangent(0);
		trajectoryBuilder:splineToLinearHeading(61, 23, 90, -90);
		actionBuilder:add(trajectoryBuilder:build());

		--park

		action = actionBuilder:build();
	end,
	start = function ()
		run(action);
	end
});