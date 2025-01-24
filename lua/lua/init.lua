local action = {};

addOpmode({
		name = "testOpmode",
		init = function()
				local trajectory = trajectoryAction(0, 0, 0);
				trajectory:lineToX(20);
				trajectory:lineToX(0);
				print(tostring(trajectory.ref));
				action = trajectory:build();
		end,
		start = function()
			run(action);
		end
})