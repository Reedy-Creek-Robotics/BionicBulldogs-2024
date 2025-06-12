require("utils");

---@type Action
local action = {}

addOpmode({
	name = "blueMiddle",
	init = function ()
		setPosEstimate(9.5, 31.5, 0);
		local builder = sequentalAction();

		builder:add(markerAction("sample 1"));

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(sleepAction(0.35))
				:add(hslideGotoMin())
				:add(intakeDown())
				:add(intakeIntake())
				:build()
			)
			:add(
				trajectoryAction(9.5, 31.5, 0)
				:setTangent(0)
				:splineToConstantHeading(21, 5, 0)
				:build()
			)
			:build()
		);

		local trajectory = nil;

		builder:add(intakeUp());
		builder:add(hslideZero());

		local parallel = parallelAction();

		local seq = sequentalAction();
		seq:add(sleepAction(0.32));
		seq:add(intakeOuttake());
		parallel:add(seq:build());

		trajectory = trajectoryAction(21, 5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, 3.5, 180);
		parallel:add(trajectory:build());

		builder:add(parallel:build());

		builder:add(markerAction("sample 2"));

		builder:add(intakeDown());
		builder:add(hslideGotoMin());
		builder:add(intakeIntake());

		trajectory = trajectoryAction(20, 3.5, 0);
		trajectory:setTangent(-90);
		trajectory:splineToConstantHeading(21, -2.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeUp());
		builder:add(hslideZero());

		seq = sequentalAction();
		seq:add(sleepAction(0.35));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(21, -2.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(18, -2.5, 180);

		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());
		builder:add(parallel:build());

		builder:add(markerAction("sample 3"));

		builder:add(
			parallelAction()
			:add(hslideGotoMin())
			:add(intakeDown())
			:add(intakeIntake())
			:add(
				trajectoryAction(18, -2.5, 0)
				:setTangent(0)
				:splineToConstantHeading(31, -2.5, 0)
				:build()
			)
			:build()
		);

		builder:add(hslideZero());
		builder:add(intakeUp());

		seq = sequentalAction();
		seq:add(sleepAction(0.5));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(31, -2.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, -2.5, 180);

		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());

		builder:add(parallel:build());

		builder:add(intakeStop());

		builder:add(markerAction("sample 4"));

		builder:add(intakeDown());
		builder:add(hslideGotoMin());
		builder:add(intakeIntake());

		trajectory = trajectoryAction(20, -2.5, 0);
		trajectory:setTangent(90);
		trajectory:splineToConstantHeading(31, 3.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeUp());
		builder:add(hslideZero());

		seq = sequentalAction();
		seq:add(sleepAction(0.5));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(31, 3.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, 3.5, 180);
		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());
		builder:add(parallel:build());
		builder:add(intakeStop());

		builder:add(markerAction("sample 5"));

		builder:add(intakeDown());
		builder:add(hslideGotoMin());
		builder:add(intakeIntake());

		trajectory = trajectoryAction(20, 3.5, 0);
		trajectory:setTangent(0);
		trajectory:splineToConstantHeading(41, 3.5, 0);
		builder:add(trajectory:build());

		builder:add(intakeUp());
		builder:add(hslideZero());

		seq = sequentalAction();
		seq:add(sleepAction(0.5));
		seq:add(intakeOuttake());

		trajectory = trajectoryAction(41, 3.5, 0);
		trajectory:setTangent(180);
		trajectory:splineToConstantHeading(20, 3.5, 180);
		parallel = parallelAction();
		parallel:add(trajectory:build());
		parallel:add(seq:build());
		builder:add(parallel:build());
		builder:add(intakeStop());

		builder:add(markerAction("score 1"));

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(sleepAction(0.675))
				:add(specimenGrab())
				:build()
			)
			:add(
				trajectoryActionX(20, 3.5, 0, overides({ maxAccel = 70 }))
				:setTangent(180)
				:splineToConstantHeading(11, 0, 180)
				:build()
			)
			:build()
		)

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(60.5, 15, -90, 90)
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

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenGrab())
				:build()
			)
			:add(
				trajectoryActionX(60.5, 15, -90, overides({ maxAccel = 70 }))
				:setTangent(-90)
				:splineToLinearHeading(11, 0, 0, 180)
				:build()
			)
			:build()
		)

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(62.5, -14, 90, -90)
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

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenGrab())
				:build()
			)
			:add(
				trajectoryActionX(62.5, -14, 90, overides({ maxAccel = 70 }))
				:setTangent(90)
				:splineToLinearHeading(11, 0, 0, 180)
				:build()
			)
			:build()
		);

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(61.5, 15, -90, 90)
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

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenGrab())
				:build()
			)
			:add(
				trajectoryActionX(61.5, 15, -90, overides({ maxAccel = 70 }))
				:setTangent(-90)
				:splineToLinearHeading(11, 0, 0, 180)
				:build()
			)
			:build()
		)

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(63.5, -14, 90, -90)
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