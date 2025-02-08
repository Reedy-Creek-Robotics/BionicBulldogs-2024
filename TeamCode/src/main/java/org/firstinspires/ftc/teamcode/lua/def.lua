---@class Opmode
---@field name string
---@field init function?
---@field start function?
---@field update function?

---@param opmode Opmode
function addOpmode(opmode) end

---@class Action
Action = {}

---@class LuaTrajectoryBuilder
---@field setTangent fun(self: table, tangent: number): LuaTrajectoryBuilder
---@field lineToX fun(self: table, x: number): LuaTrajectoryBuilder
---@field lineToX2 fun(self: table, x: number, vel: number, minAccel: number, maxAccel: number): LuaTrajectoryBuilder
---@field lineToY fun(self: table, y: number): LuaTrajectoryBuilder
---@field lineToY2 fun(self: table, y: number, vel: number, minAccel: number, maxAccel: number): LuaTrajectoryBuilder
---@field turnTo fun(self: table, h: number): LuaTrajectoryBuilder
---@field splineTo fun(self: table, x: number, y: number, t: number): LuaTrajectoryBuilder
---@field splineTo2 fun(self: table, x: number, y: number, t: number, vel: number, minAccel: number, maxAccel: number): LuaTrajectoryBuilder
---@field splineToLinearHeading fun(self: table, x: number, y: number, h: number, t: number): LuaTrajectoryBuilder
---@field splineToLinearHeading2 fun(self: table, x: number, y: number, h: number, t: number, vel: number, minAccel: number, maxAccel: number): LuaTrajectoryBuilder
---@field splineToConstantHeading fun(self: table, x: number, y: number, t: number): LuaTrajectoryBuilder
---@field splineToConstantHeading2 fun(self: table, x: number, y: number, t: number, vel: number, minAccel: number, maxAccel: number): LuaTrajectoryBuilder
---@field build fun(self: table): Action
---@field getEndX fun(self: table): number
---@field getEndY fun(self: table): number
---@field getEndH fun(self: table): number
LuaTrajectoryBuilder = {}

---@class LuaSequentalAction
---@field add fun(self: table, action: Action): LuaSequentalAction
---@field build fun(self: table): Action
LuaSequentalAction = {}

---@class LuaParallelAction 
---@field add fun(self: table, action: Action): LuaParallelAction
---@field build fun(self: table): Action
LuaParallelAction = {}

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
function specimenGrabInstant() end

---@return Action
function specimenScore() end

---@param action Action
function run(action) end

---@param action Action
---@param filename string
function runTimer(action, filename) end

---@param x number
---@param y number
---@param h number
function setPosEstimate(x, y, h) end