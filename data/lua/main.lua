local pos = 0
local dir = 0

---@type Opmode[]
Opmodes = {
	require("opmodes.main"),
	require("opmodes.main2"),
	{
		name = "test thing",
		start = function()
			servos.setPos(0)
		end,
		---@param time Time
		update = function(time)
			if dir == 0 then
				pos = pos + time.delta
				if pos >= 1 then
					dir = 1
				end
			else
				pos = pos - time.delta
				if pos <= 0 then
					dir = 0
				end
			end
			servos.setPos(pos)
		end,
	},
}