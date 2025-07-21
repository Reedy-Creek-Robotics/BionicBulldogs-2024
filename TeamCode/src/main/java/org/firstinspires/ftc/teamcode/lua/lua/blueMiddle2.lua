require("utils");

---@type Action
local action = {}

---@type Opmode
local opmode = { name = "blueMiddle2" };

function opmode.init()
	setPosEstimate(9.5, 31.5, 180);
	local builder = sequentalAction();

	builder:add(
		markerSequentialAction("score preload")
		:add(specimenGrab())
		--:add(
		--	parallelAction()
		:add(
			trajectoryAction(9.5, 31.5, 180)
			:setTangent(0)
			:splineToLinearHeading(60.5, 15, -90, 90)
			:build()
		)
		--:add(
		--	sequentalAction()
		--:add(sleepAction(2))
		:add(specimenScore())
		--	:build()
		--)
		--:build()
		--)
		:build()
	);

	builder:add(
		markerSequentialAction("sample 1")
		:add(
			trajectoryAction(60.5, 15, -90)
			:setTangent(-90)
			:splineToConstantHeading(48, 8, 180)
			:build()
		)
		:add(
			sequentalAction()
			:add(sleepAction(0.35))
			:add(hslideGotoMin())
			:add(intakeDown())
			:add(intakeIntake())
			:build()
		)
		:add(
			trajectoryAction(48, 8, -90)
			:setTangent(180)
			:splineToConstantHeading(19.5, -3.5, 0)
			:build()
		)
		:add(intakeUp())
		:add(hslideZero())
		:add(sleepAction(0.32))
		:add(intakeOuttake())
		:add(sleepAction(0.3))
		:build()
	);

	builder:add(
		markerSequentialAction("score 1")
		:add(
			trajectoryActionX(19.5, -3.5, 0, overides({ maxAccel = 70 }))
			:setTangent(180)
			:splineToConstantHeading(11, 0, 180)
			:build()
		)
		:add(specimenGrab())

		:add(
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
		)
		:build()
	);

	builder:add(
		markerSequentialAction("score 2")

		:add(
			trajectoryActionX(60.5, 15, -90, overides({ maxAccel = 70 }))
			:setTangent(-90)
			:splineToLinearHeading(11, 0, 0, 180)
			:build()
		)
		:add(specimenGrab())

		:add(
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
		)
		:build()
	);

	builder:add(
		markerSequentialAction("score 3")

		:add(
			trajectoryActionX(61.5, 15, -90, overides({ maxAccel = 70 }))
			:setTangent(-90)
			:splineToLinearHeading(11, 0, 0, 180)
			:build()
		)
		:add(specimenGrab())

		:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(62.5, 15, -90, 90)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenScore())
				:build()
			)
			:build()
		)
		:build()
	);

	builder:add(
		markerSequentialAction("score 4")

		:add(
			trajectoryActionX(62.5, 15, -90, overides({ maxAccel = 70 }))
			:setTangent(-90)
			:splineToLinearHeading(11, 0, 0, 180)
			:build()
		)
		:add(specimenGrab())

		:add(
			parallelAction()
			:add(
				trajectoryAction(11, 0, 0)
				:setTangent(0)
				:splineToLinearHeading(63.5, 15, -90, 90)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(2))
				:add(specimenScore())
				:build()
			)
			:build()
		)
		:build()
	);

	builder:add(sleepAction(1));

	action = builder:build();
end

function opmode.start()
	runTimer(action, "blueMiddle2.txt");
end

addOpmode(opmode);