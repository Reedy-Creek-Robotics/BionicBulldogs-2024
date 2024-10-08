package org.firstinspires.ftc.teamcode.modules

import kotlin.math.pow
import kotlin.math.sqrt

class Vec2
{
		var x: Float;
		var y: Float;

		constructor(x2: Float, y2: Float)
		{
				x = x2;
				y = y2;
		}

		constructor(value: Float)
		{
				x = value;
				y = value;
		}

		constructor()
		{
				x = 0f;
				y = 0f;
		}

		fun add(other: Vec2): Vec2
		{
				return Vec2(x + other.x, y + other.y);
		}

		fun sub(other: Vec2): Vec2
		{
				return Vec2(x - other.x, y - other.y);
		}

		fun mul(other: Vec2): Vec2
		{
				return Vec2(x * other.x, y * other.y);
		}

		operator fun div(other: Vec2): Vec2
		{
				return Vec2(x / other.x, y / other.y);
		}

		fun length(): Float
		{
				return sqrt(x.toDouble().pow(2.0) + y.toDouble().pow(2.0)).toFloat();
		}

		fun neg(): Vec2
		{
				return Vec2(-x, -y);
		}
}