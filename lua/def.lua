---@class Opmode
---@field name string
---@field init function?
---@field start function?
---@field update function?

---@class opmode Opmode
function addOpmode(opmode) end

---@class Action

---@class LuaTrajectoryBuilder
---@field setTangent fun(self: table, tangent: number)
---@field lineToX fun(self: table, x: number)
---@field lineToY fun(self: table, y: number)
---@field splineToLinearHeading fun(self: table, x: number, y: number, h: number, t: number)
---@field splineToConstantHeading fun(self: table, x: number, y: number, t: number)
---@field build fun(): Action

---@return LuaTrajectoryBuilder
---@param x number
---@param y number
---@param h number
function trajectoryAction(x, y, h) end

---@param action Action
function run(action) end