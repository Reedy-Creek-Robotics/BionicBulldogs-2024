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

		trajectory = trajectoryActionX(20, 3.5, 0, overides({ maxAccel = 70 }));
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(11, 0, 180);
		builder:add(trajectory:build());

		builder:add(specimenGrab());

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(60.5, 15.5, -90, 90)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenScore())
				:build()
			)
			:build()
		);

		builder:add(markerAction("score 2"));

		trajectory = trajectoryActionX(60.5, 15.5, -90, overides({ maxAccel = 70 }));
		trajectory:setTangent(-90);
		trajectory:splineToLinearHeading(11, 0, 0, 180);
		builder:add(trajectory:build());

		builder:add(specimenGrab());

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(60.5, -14, 90, -90)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenScore())
				:build()
			):build()
		);

		builder:add(markerAction("score 3"));

		trajectory = trajectoryActionX(60.5, -14, 90, overides({ maxAccel = 70 }));
		trajectory:setTangent(90);
		trajectory:splineToLinearHeading(11, 0, 0, 180);
		builder:add(trajectory:build());

		builder:add(specimenGrab());

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(61.5, 15.5, -90, 90)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenScore())
				:build()
			)
			:build()
		);

		builder:add(markerAction("score 4"));

		trajectory = trajectoryActionX(61.5, 15.5, -90, overides({ maxAccel = 70 }));
		trajectory:setTangent(-90);
		trajectory:splineToLinearHeading(11, 0, 0, 180);
		builder:add(trajectory:build());

		builder:add(specimenGrab());

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(61.5, -14, 90, -90)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenScore())
				:build()
			):build()
		);

		builder:add(sleepAction(1));

		action = builder:build();
	end,
	start = function ()
		runTimer(action, "blueMiddle.txt");
	end
});