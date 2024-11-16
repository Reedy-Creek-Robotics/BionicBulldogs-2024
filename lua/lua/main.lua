local function score()
	telem.addData("score", "")
	telem.update()
end

---@type Opmode[]
Opmodes = {
	{
		name = "testAuto",
		path = "rotate.path",
		start = function(recognition)
			claw.close()
			slides.runToPosition2(-1300)
		end,
		markers = {
			score,
		},
	},
}
