require("utils");

---@type Action
local action = {}

addOpmode({
	name = "blueMiddle",
	init = function ()
		setPosEstimate(9.5, 31.5, 0);
		local builder = sequentalAction();

		builder:add(markerAction("sample 1"));

		builder:add(hslideGotoPos(0.95));
		builder:add(intakeDown());
		builder:add(intakeIntake());

		local trajectory = trajectoryAction(9.5, 31.5, 0);
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(24, 3.5, -90);
		builder:add(trajectory:build());


		trajectory = trajectoryActionX(24, 3.5, 0, overides({ vel = 40 }));
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(27, 3.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeStop());
		builder:add(intakeUp());
		builder:add(hslideZero());

		local parallel = parallelAction();

		local seq = sequentalAction();
		seq:add(sleepAction(0.4));
		seq:add(intakeOuttake());
		parallel:add(seq:build());

		trajectory = trajectoryAction(27, 3.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, 3.5, 180);
		parallel:add(trajectory:build());

		builder:add(parallel:build());
		builder:add(intakeStop());

		builder:add(markerAction("sample 2"));

		builder:add(hslideGotoPos(0.95));
		builder:add(intakeDown());
		builder:add(intakeIntake());

		trajectory = trajectoryAction(20, 3.5, 0);
		trajectory:setTangent(-90);
		trajectory:splineToConstantHeading(22, -2.5, 0);
		builder:add(trajectory:build());

		trajectory = trajectoryActionX(22, -2.5, 0, overides({ vel = 20, maxAccel = 50 }));
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(27, -2.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeStop());
		builder:add(intakeUp());
		builder:add(hslideZero());

		seq = sequentalAction();
		seq:add(sleepAction(0.4));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(27, -2.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(18, -2.5, 180);

		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());
		builder:add(parallel:build());

		builder:add(intakeStop());

		builder:add(markerAction("sample 3"));

		builder:add(hslideGotoPos(0.95));
		builder:add(intakeDown());

		builder:add(intakeIntake());
		trajectory = trajectoryAction(18, -2.5, 0);
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(37, -2.5, 0);
		builder:add(trajectory:build());

		trajectory = trajectoryActionX(37, -2.5, 0, overides({ vel = 40 }));
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(39, -2.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeStop());
		builder:add(hslideZero());
		builder:add(intakeUp());

		seq = sequentalAction();
		seq:add(sleepAction(0.4));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(39, -2.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, -2.5, 180);

		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());

		builder:add(parallel:build());

		builder:add(intakeStop());

		builder:add(markerAction("sample 4"));

		builder:add(hslideGotoPos(0.95));
		builder:add(intakeDown());

		builder:add(intakeIntake());
		trajectory = trajectoryAction(20, -2.5, 0);
		trajectory:setTangent(90);
		trajectory:splineToConstantHeading(37, 3.5, 0);
		builder:add(trajectory:build());

		trajectory = trajectoryActionX(37, 3.5, 0, overides({ vel = 40 }));
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(39, 3.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeStop());
		builder:add(intakeUp());
		builder:add(hslideZero());

		seq = sequentalAction();
		seq:add(sleepAction(0.4));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(39, 3.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, 3.5, 180);
		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());
		builder:add(parallel:build());
		builder:add(intakeStop());

		builder:add(markerAction("score 1"));

		trajectory = trajectoryAction(20, 3.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(11, 0, 180);
		builder:add(trajectory:build());

		builder:add(specimenGrab());

		trajectory = trajectoryAction(11, 0, 0);
		trajectory:setTangent(0);
		trajectory:splineToLinearHeading(60.5, 15.5, -90, 90);
		builder:add(trajectory:build());

		builder:add(specimenScore());

		builder:add(markerAction("score 2"));

		trajectory = trajectoryAction(60.5, 15.5, -90);
		trajectory:setTangent(-90);
		trajectory:splineToLinearHeading(11, 0, 0, 180);
		builder:add(trajectory:build());

		builder:add(specimenGrab());

		trajectory = trajectoryAction(11, 0, 0);
		trajectory:setTangent(0);
		trajectory:splineToLinearHeading(60.5, -15.5, 90, -90);
		builder:add(trajectory:build());

		builder:add(specimenScore());


		builder:add(sleepAction(3));

		action = builder:build();
	end,
	start = function ()
		runTimer(action, "blueMiddle.txt");
	end
});