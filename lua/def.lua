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
---@field build fun(self: table): Action

---@class LuaSequentalAction
---@field add fun(self: table, action: Action)
---@field build fun(self: table): Action

---@class LuaParallelAction 
---@field add fun(self: table, action: Action)
---@field build fun(self: table): Action

---@return LuaTrajectoryBuilder
---@param x number
---@param y number
---@param h number
function trajectoryAction(x, y, h) end

---@return LuaTrajectoryBuilder
---@param x number
---@param y number
---@param h number
---@param vel number
---@param minAccel number
---@param maxAccel number
function trajectoryActionX(x, y, h, vel, minAccel, maxAccel) end

---@return LuaSequentalAction
function sequentalAction() end

---@return LuaParallelAction
function parallelAction() end

---@return Action
---@param time number
function sleepAction(time) end

---@return Action
function specimenGrab() end

---@return Action
function specimenScore() end

---@param action Action
function run(action) end

---@param action Action
function runTimer(action) end

---@param x number
---@param y number
---@param h number
function setPosEstimate(x, y, h) end