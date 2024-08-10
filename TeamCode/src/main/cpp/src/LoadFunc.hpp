#pragma once
#include <lua/lua.hpp>
#include <jni.h>
void loadFuncs(lua_State* l);
void addObject(jobject object);
void deleteRefs();
