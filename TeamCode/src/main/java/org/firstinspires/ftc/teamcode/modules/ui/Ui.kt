package org.firstinspires.ftc.teamcode.modules.ui

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.Telemetry

/*
up - dpad up;
down - dpad down;
select/inc - dpad right;
dec - dpad left;
*/
class UI(private val telemetry: Telemetry, private val gamepad: Gamepad)
{
	private var itemCount = 0
	private var selectedItem = 1
	private var prevUp = false
	private var prevDown = false
	private var prevRight = false
	private var prevLeft = false
	
	fun update()
	{
		if(gamepad.dpad_up && !prevUp)
		{
			selectedItem--
			prevUp = true
		}
		else if(!gamepad.dpad_up)
		{
			prevUp = false
		}
		if(gamepad.dpad_down && !prevDown)
		{
			selectedItem++
			prevDown = true
		}
		else if(!gamepad.dpad_down)
		{
			prevDown = false
		}
		itemCount = 0
		telemetry.update()
	}
	
	fun resetSelected()
	{
		selectedItem = 1
	}
	
	fun button(label: String): Boolean
	{
		itemCount++
		addLine(label)
		if(selectedItem == itemCount)
		{
			if(right() && !prevRight)
			{
				prevRight = true
				return true
			}
			else if(!right())
			{
				prevRight = false
			}
		}
		return false
	}
	
	fun floatInput(label: String, value: FloatPtr, step: Float): Boolean
	{
		itemCount++
		addLine(label + ": " + value.value)
		if(selectedItem == itemCount)
		{
			if(right() && !prevRight)
			{
				prevRight = true
				value.value += step
				return true
			}
			else if(!right())
			{
				prevRight = false
			}
			if(left() && !prevLeft)
			{
				prevLeft = true
				value.value -= step
				return true
			}
			else if(!left())
			{
				prevLeft = false
			}
		}
		return false
	}
	
	fun intInput(label: String, value: IntPtr, step: Int): Boolean
	{
		itemCount++
		addLine(label + ": " + value.value)
		if(selectedItem == itemCount)
		{
			if(right() && !prevRight)
			{
				prevRight = true
				value.value += step
				return true
			}
			else if(!right())
			{
				prevRight = false
			}
			if(left() && !prevLeft)
			{
				prevLeft = true
				value.value -= step
				return true
			}
			else if(!left())
			{
				prevLeft = false
			}
		}
		return false
	}
	
	fun checkbox(label: String, value: BoolPtr): Boolean
	{
		itemCount++
		addLine(label + ": " + if(value.value) "[V]" else "[X]")
		if(selectedItem == itemCount)
		{
			if(right() && !prevRight)
			{
				prevRight = true
				value.value = !value.value
				return true
			}
			else if(!right())
			{
				prevRight = false
			}
		}
		return false
	}
	
	fun label(label: String)
	{
		telemetry.addLine("   $label")
	}
	
	fun label(label: String, arg: String?)
	{
		telemetry.addLine("   $label: $arg")
	}
	
	fun addLine(label: String)
	{
		if(selectedItem == itemCount)
		{
			telemetry.addLine(">>$label")
		}
		else
		{
			telemetry.addLine("--$label")
		}
	}
	
	fun right(): Boolean
	{
		return gamepad.cross;
	}
	
	fun left(): Boolean
	{
		return gamepad.triangle;
	}
}