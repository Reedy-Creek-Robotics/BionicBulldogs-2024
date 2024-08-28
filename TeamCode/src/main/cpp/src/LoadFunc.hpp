#pragma once
#include <jni.h>
#include <lua/lua.hpp>
void endClass(lua_State* l, const char* name);
void startClass();
void addFunction(jstring name, jstring signature, int rtn, int argc, lua_State* l);
void loadFuncs(lua_State* l);
void addObject(jobject object);
void deleteRefs();
