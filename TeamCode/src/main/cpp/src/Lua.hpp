#pragma once

#include <string>

void callNextDispMarker(std::string str);
void jniErr(const std::string& msg);
void print(const char* msg);
void stop();
std::string getPathName(const std::string& name);
