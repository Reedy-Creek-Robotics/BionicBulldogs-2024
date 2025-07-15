---@return Action
---@param x number
---@param y number
function grabSample(x, y)
	return markerSequentialAction("grab sample")
			:add(recognizeSample())
			:add(moveToSample(x, y, 0))
			:add(extendHSlideToSample(-2 - 9))
			:add(sleepAction(0.5))
			:add(intakeDown())
			:add(sleepAction(0.5))
			:add(intakeIntake())
			:add(extendHSlideToSample(-9))
			:add(
				waitForOtherAction(
					intakeWaitForColor(Colors.YELLOW, 999),
					sequentalAction()
					:add(sleepAction(1))
					:add(moveToSample(x, y + 2, 90))
					:add(moveToSample(x, y - 2, -90))
					:add(extendHSlideToSample(2 - 9))
					:add(moveToSample(x, y, 90))
					:build()
				)
			)
			:build();
end