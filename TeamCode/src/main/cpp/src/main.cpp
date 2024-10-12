#include <jni.h>
#include <lua/lua.hpp>
#include <vector>

#include "print.hpp"

#define internalInit Java_org_firstinspires_ftc_teamcode_opmodeloader_OpmodeLoader_internalInit

std::vector<std::string> opmodes;

void fieldErr(lua_State* l, const char* fieldName, const char* typeName)
{
	const char* name = luaL_typename(l, -1);
	luaL_error(l, "bad type for field '%s', expected '%s', got '%s'", fieldName, typeName, name);
}

int addOpmode(lua_State* l)
{
	int top = lua_gettop(l);
	if (top != 1)
		luaL_error(l, "expected 1 argument");
	if (lua_type(l, 1) != LUA_TTABLE)
		luaL_typeerror(l, 1, "table");

	lua_getfield(l, -1, "name");
	if (lua_type(l, -1) != LUA_TSTRING)
	{
		fieldErr(l, "name", "string");
	}
  const char* name = lua_tostring(l, -1);
  lua_pop(l, 1);

	lua_getfield(l, -1, "start");
  char type = lua_type(l, -1);
	if (type != LUA_TNIL && type != LUA_TFUNCTION)
	{
		fieldErr(l, "start", "function?");
	}
  lua_pop(l, 1);

	lua_getfield(l, -1, "update");
  type = lua_type(l, -1);
	if (type != LUA_TNIL && type != LUA_TFUNCTION)
	{
		fieldErr(l, "update", "function?");
	}
  lua_pop(l, 1);

	lua_getfield(l, -1, "path");
  type = lua_type(l, -1);
	if (type != LUA_TNIL && type != LUA_TSTRING)
	{
		fieldErr(l, "path", "string?");
	}
  lua_pop(l, 1);

	lua_getfield(l, -1, "markers");
  type = lua_type(l, -1);
	if (type != LUA_TNIL && type != LUA_TTABLE)
	{
		fieldErr(l, "markers", "table?");
	}
  lua_pop(l, 1);

  opmodes.push_back(name);
	return 0;
}

lua_State* l = nullptr;

extern "C" JNIEXPORT jobjectArray JNICALL internalInit(JNIEnv* env, jobject thiz, jobject stdlib)
{
	initFuncs(stdlib, env);

	jclass clazz = env->GetObjectClass(stdlib);
	jmethodID fun = env->GetMethodID(clazz, "getDataDir", "()Ljava/lang/String;");

	jstring obj = (jstring)env->CallObjectMethod(stdlib, fun);

	const char* str = env->GetStringUTFChars(obj, nullptr);
	std::string dataDir = str;
	env->ReleaseStringUTFChars(obj, str);

	if (l)
		lua_close(l);
	l = luaL_newstate();

	luaL_openlibs(l);

	luaL_dostring(l, ("package.path = package.path .. ';" + dataDir + "/lua/?.lua'").c_str());

	lua_pushcfunction(l, addOpmode);
	lua_setglobal(l, "addOpmode");

  lua_newtable(l);

	if (luaL_dofile(l, (dataDir + "/lua/main.lua").c_str()))
	{
		const char* e = lua_tostring(l, -1);
		err(e);
    return nullptr;
	}

  lua_pushnil(l);

	jobjectArray arr = env->NewObjectArray(opmodes.size(), env->FindClass("java/lang/String"), env->NewStringUTF("T"));

  int idx = 0;
  for(const std::string& opmode : opmodes)
  {
    jstring str = env->NewStringUTF(opmode.c_str());
    env->SetObjectArrayElement(arr, idx++, str);
    env->DeleteLocalRef(str);
  }

	return arr;
}
