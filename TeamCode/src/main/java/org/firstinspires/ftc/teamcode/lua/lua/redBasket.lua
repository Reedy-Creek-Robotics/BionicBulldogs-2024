require("utils");

---@type Action
local action = {}

--local prevPos = { x = 0, y = 0, h = 0 };
--
--LuaTrajectoryBuilder.build2 = LuaTrajectoryBuilder.build;
--LuaTrajectoryBuilder.build = nil;
--
--function LuaTrajectoryBuilder:build()
--	prevPos.x = self:getEndX();
--	prevPos.y = self:getEndY();
--	prevPos.h = self:getEndH();
--	return self:build2();
--end
--
--function prevPos:get()
--	return self.x, self.y, self.h;
--end

addOpmode({
	name = "redBasket",
	init = function ()
		setPosEstimate(7.5, 30.5, 90);
		--setPosEstimate(7.5, 39.5, 90);
		builder = sequentalAction();
		builder:add(markerAction("score spec"));
		builder:add(specimenGrabInstant())
		builder:add(
			trajectoryAction(7.5, 30.5, 90)
			:setTangent(0)
			:splineToConstantHeading(63, 33.5, -22.5)
			--trajectoryAction(7.5, 39.5, 90)
			--:setTangent(0)
			--:splineToConstantHeading(63, 39.5, 0)
			--:setTangent(-90)
			--:splineToConstantHeading(63, 33.5, -90)
			:build()
		)
		builder:add(specimenScore());

		builder:add(markerAction("grab 1"));

		builder:add(
			trajectoryAction(63, 33.5, 90)
			:turnTo(143)
			:build()
		);

		builder:add(intakeDown());
		builder:add(intakeIntake());
		builder:add(hslideGotoPos(0.6));

		builder:add(sleepAction(0.55));

		builder:add(markerAction("score 1"));

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(intakeUp())
				:add(hslideZero())
				:add(sleepAction(0.2))
				:add(sampleClawTransfer())
				:add(sampleClawUp())
				:build()
			)
			:add(
				trajectoryAction(63, 33.5, 140)
				:setTangent(90)
				:splineToLinearHeading(15, 54, -45, 135)
				:build()
			)
			:build()
		);

		builder:add(sampleClawScore());

		builder:add(markerAction("grab 2"));

		builder:add(intakeDown());
		builder:add(intakeIntake());

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(16, 53, -45)
				:setTangent(0)
				:splineToLinearHeading(21, 53, 13, 0)
				--:turnTo(15)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(0.6))
				:add(hslideGotoMin())
				:build()
			)
			:build()
		);

		builder:add(sleepAction(0.65));

		builder:add(markerAction("score 2"));

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(intakeUp())
				:add(hslideZero())
				:add(sleepAction(0.65))
				:add(sampleClawTransfer())
				:add(sampleClawUp())
				:add(sleepAction(1))
				:build()
			)
			:add(
				trajectoryAction(21, 53, 13)
				:setTangent(90)
				:splineToLinearHeading(15, 54, -45, 135)
				:build()
			)
			:build()
		);

		builder:add(sampleClawScore());

		builder:add(markerAction("grab 3"));

		builder:add(intakeDown());
		builder:add(intakeIntake());

		builder:add(
			parallelAction()
			:add(
				trajectoryAction(16, 53, -45)
				:setTangent(0)
				:splineToLinearHeading(21, 53, 35, 0)
				:build()
			)
			:add(
				sequentalAction()
				:add(sleepAction(0.6))
				:add(hslideGotoMin())
				:build()
			)
			:build()
		);

		builder:add(sleepAction(0.65));

		builder:add(markerAction("score 3"));

		builder:add(
			parallelAction()
			:add(
				sequentalAction()
				:add(intakeUp())
				:add(hslideZero())
				:add(sleepAction(0.65))
				:add(sampleClawTransfer())
				:add(sampleClawUp())
				:add(sleepAction(1))
				:build()
			)
			:add(
				trajectoryAction(21, 53, 35)
				:setTangent(90)
				:splineToLinearHeading(15, 54, -45, 135)
				:build()
			)
			:build()
		);

		builder:add(sampleClawScore());

		builder:add(sleepAction(1));

		action = builder:build();
	end,
	start = function ()
		runTimer(action, "redBasket.txt");
	end
})