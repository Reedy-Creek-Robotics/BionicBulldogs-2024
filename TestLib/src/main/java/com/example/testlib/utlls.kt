package com.example.testlib

val startTime = timeNow();

/**
 * @return milliseconds
 */
fun timeNow(): Long
{
	return System.currentTimeMillis();
}