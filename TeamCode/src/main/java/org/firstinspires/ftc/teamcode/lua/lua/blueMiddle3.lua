require("utils");

---@type Action
local action = {}

---@type Opmode
local opmode = { name = "blueMiddle3" };

function opmode.init()
	setPosEstimate(9.5, 31.5, 180);
	local builder = sequentalAction();

	builder:add(
		markerSequentialAction("sample 1")
		:add(
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
				:splineToConstantHeading(19.5, 3.5, 0)
				:build()
			)
			:build()
		)
		:add(intakeUp())
		:add(hslideZero())
		:add(sleepAction(0.42))
		:add(intakeOuttake())
		:add(sleepAction(0.3))
		:build()
	);

	builder:add(
		markerSequentialAction("sample 2")
		:add(intakeDown())
		:add(hslideGotoMin())
		:add(intakeIntake())
		:add(
			trajectoryAction(19.5, 3.5, 0)
			:setTangent(90)
			:splineToConstantHeading(30, 4, 0)
			:build()
		)
		:add(intakeUp())
		:add(hslideZero())
		:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(sleepAction(0.6))
				:add(intakeOuttake())
				:build()
			)
			:add(
				trajectoryAction(30, 4, 0)
				:setTangent(180)
				:splineToConstantHeading(20, 3.2, 180)
				:build()
			)
			:build()
		)
		:add(intakeStop())
		:build()
	);

	builder:add(sleepAction(1));

	action = builder:build();
end

function opmode.start()
	runTimer(action, "blueMiddle2.txt");
end

addOpmode(opmode);