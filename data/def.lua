---@meta
---@param msg string
function print(msg) end

---@param time number
function delay(time) end

function checkRunning() end

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

servos = {}
---@param pos number
function servos.setPos(pos) end

---@param pos number
function servos.setPos2(pos) end