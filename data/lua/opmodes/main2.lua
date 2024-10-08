---@type Opmode
local opmode = {
	name = "main2",
}

---@param recognition number
function opmode.start(recognition)
	--servos.setPos(0)
	--servos.setPos2(0.5)
end

---@type number
local t = 0

---@type boolean
local dir = true

---@param time Time
function opmode.update(time)
	servos.setPos(t)
	if dir then
		t = t + time.delta * 0.5
	else
		t = t - time.delta * 0.5
	end
	if t >= 1 then
		dir = false
	elseif t <= 0 then
		stop()
	end
	checkRunning()
end

return opmode
