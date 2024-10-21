addOpmode({
	name = "opmode",
	start = function()
		telem.addData("lua", "did dis work")
		telem.update()
		delay(4)
	end,
})
