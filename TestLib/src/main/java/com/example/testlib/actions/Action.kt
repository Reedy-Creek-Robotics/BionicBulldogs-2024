package com.example.testlib.actions

interface Action
{
	fun start();
	fun update(): Boolean;
}