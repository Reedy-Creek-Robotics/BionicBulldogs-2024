local defaultVel = 80;
local defaultMinAccel = -30;
local defaultMaxAccel = 60;

---@class Overrides
---@field vel number?
---@field minAccel number?
---@field maxAccel number?

---@param table Overrides
---@return number, number, number
function overrides(table)
	local vel = table.vel or defaultVel;
	local minAccel = table.minAccel or defaultMinAccel;
	local maxAccel = table.maxAccel or defaultMaxAccel;
	return vel, minAccel, maxAccel;
end