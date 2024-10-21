#pragma once

#include <jni.h>
#include <lua/lua.hpp>

void initFunctionBuilder(JNIEnv* env, lua_State* l);
void clearRefs(JNIEnv* env);
void setEnv(JNIEnv* env);
