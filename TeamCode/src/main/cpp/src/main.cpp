#include "functionBuilder.hpp"
#include <cstring>
#include <jni.h>
#include <lua/lua.hpp>
#include <vector>

#include "print.hpp"

#define init Java_org_firstinspires_ftc_teamcode_opmodeloader_OpmodeLoader_internalInit
#define loadOpmode Java_org_firstinspires_ftc_teamcode_opmodeloader_OpmodeLoader_internalLoadOpmode
#define start Java_org_firstinspires_ftc_teamcode_opmodeloader_OpmodeLoader_internalStart
#define update Java_org_firstinspires_ftc_teamcode_opmodeloader_OpmodeLoader_update

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

	lua_getglobal(l, "data");
	lua_pushvalue(l, 1);
	lua_setfield(l, -2, std::to_string(opmodes.size()).c_str());
	lua_setglobal(l, "data");

	opmodes.push_back(name);
	return 0;
}

lua_State* l = nullptr;

extern "C" JNIEXPORT jobjectArray JNICALL init(JNIEnv* env, jobject thiz, jobject stdlib)
{
	opmodes.clear();

	clearRefs(env);
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
	lua_setglobal(l, "data");

	if (luaL_dofile(l, (dataDir + "/lua/main.lua").c_str()))
	{
		const char* e = lua_tostring(l, -1);
		err(e);
		return nullptr;
	}

	lua_pushnil(l);

	initFunctionBuilder(env, l);

	jobjectArray arr = env->NewObjectArray(opmodes.size(), env->FindClass("java/lang/String"), env->NewStringUTF("T"));

	int idx = 0;
	for (const std::string& opmode : opmodes)
	{
		jstring str = env->NewStringUTF(opmode.c_str());
		env->SetObjectArrayElement(arr, idx++, str);
		env->DeleteLocalRef(str);
	}
	return arr;
}

int opmodeIndex = -1;

extern "C" JNIEXPORT void JNICALL loadOpmode(JNIEnv* env, jobject thiz, jstring name)
{
	const char* currentOpmode = env->GetStringUTFChars(name, nullptr);
	std::string opmodeName = currentOpmode;
	env->ReleaseStringUTFChars(name, currentOpmode);

	for (int i = 0; i < opmodes.size(); i++)
	{
		if (opmodes[i] == opmodeName)
		{
			opmodeIndex = i;
			break;
		}
	}

	if (opmodeIndex == -1)
	{
		char msg[64];
		memset(msg, 0, 64);
		sprintf(msg, "cant find opmode '%s'", opmodeName.c_str());
		err(msg);
	}
}


extern "C" JNIEXPORT void JNICALL start(JNIEnv* env, jobject thiz, int recognition)
{
	setEnv(env);
	std::string s = std::to_string(opmodeIndex);
	lua_settop(l, 0);
	lua_getglobal(l, "data");
	lua_getfield(l, -1, s.c_str());

	lua_getfield(l, -1, "start");
	if (lua_type(l, -1) == LUA_TFUNCTION)
	{
		lua_pushnumber(l, recognition);
		if (lua_pcall(l, 1, 0, 0))
		{
			err(lua_tostring(l, -1));
		}
	}
}

extern "C" JNIEXPORT void JNICALL update(JNIEnv* env, jobject thiz, double delta, double elapsed)
{
	lua_getfield(l, -1, "start");
	if (lua_type(l, -1) == LUA_TFUNCTION)
	{
    lua_newtable(l);
		lua_pushnumber(l, elapsed);
    lua_setfield(l, -2, "elapsedTime");
		lua_pushnumber(l, delta);
    lua_setfield(l, -2, "deltaTime");
		if (lua_pcall(l, 1, 0, 0))
		{
			err(lua_tostring(l, -1));
		}
	}
}
