package org.firstinspires.ftc.teamcode.opmode.auto.Actions

interface Action
{
	fun start();
	fun update(): Boolean;
}