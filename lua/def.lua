---@meta
---@param msg string
function print(msg) end

---@param time number
function delay(time) end

function checkRunning() end

function stop() end

telem = {}

---@param lbl string
---@param msg string
function telem.addData(lbl, msg) end

function telem.update() end

---@class Opmode
---@field name string
---@field path string?
---@field start function?
---@field update function?
---@field markers any[function]?

---@class Time
---@field delta number
---@field elapsed number

slides = {}
---@param pos number
function slides.runToPosition2(pos) end
---@return boolean
function slides.isBusy() end

claw = {}
function claw.open() end
function claw.close() end