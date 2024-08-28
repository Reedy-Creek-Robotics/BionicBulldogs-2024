#include "JFunc.hpp"
#include "LoadFunc.hpp"
#include "Lua.hpp"
#include "macros/Helpers.hpp"

#include <lua/lua.hpp>
#include <string>
#include <unordered_map>
#include <vector>

bool inClass = false;

void startClass()
{
	inClass = true;
}

void endClass(lua_State* l, const char* name)
{
	inClass = false;
	lua_setglobal(l, name);
}

std::vector<JFunc2> funcs;

void errCheck()
{
	JNIEnv* env = FuncStat::env;
	if (env->ExceptionCheck())
	{
		env->ExceptionDescribe();
		env->ExceptionClear();
		jniErr("JNI error, check logs for error message");
	}
}

std::unordered_map<std::string, jobject> objects = {};

int callJFunc(lua_State* l)
{
	int argc = lua_gettop(l);
	lua_getfield(l, 1, "id");

	int i = lua_tointeger(l, -1);

	JFunc2& fun = funcs[i];

	if (fun.argc != argc)
	{
		luaL_error(l, ("expected " + std::to_string(fun.argc) + " args, got " + std::to_string(argc)).c_str());
	}

	jvalue* args = new jvalue[argc];

	for (int i = 0; i < argc; i++)
	{
		int type = lua_type(l, i + 1);
		if (type == LUA_TNUMBER)
			args[i].f = lua_tonumber(l, i + 1);
		if (type == LUA_TBOOLEAN)
			args[i].z = lua_toboolean(l, i + 1);
		if (type == LUA_TSTRING)
		{
			const char* str = lua_tostring(l, i + 1);
			args[i].l = FuncStat::env->NewStringUTF(str);
		}
	}

	switch (fun.rtnType)
	{
	case LUA_TNONE:
		fun.callV(args);
		delete[] args;
		return 0;
	case LUA_TNUMBER: {
		float rtn = fun.callF(args);
		lua_pushnumber(l, rtn);
		delete[] args;
		return 1;
	}
	case LUA_TBOOLEAN: {
		bool rtn = fun.callB(args);
		lua_pushboolean(l, rtn);
		delete[] args;
		return 1;
	}
	case LUA_TSTRING:
		jstring rtn = (jstring)fun.call(args);
		const char* str = FuncStat::env->GetStringUTFChars(rtn, nullptr);
		lua_pushstring(l, str);
		FuncStat::env->ReleaseStringUTFChars(rtn, str);
		delete[] args;
		return 1;
	}
	return 0;
}

void loadFuncs(lua_State* l)
{
	luaL_newmetatable(l, "jfunc");
	lua_pushcfunction(l, callJFunc);
	lua_setfield(l, -2, "__call");
	lua_pop(l, 1);
}

void addObject(jobject object)
{
	jclass clazz = FuncStat::env->GetObjectClass(object);
	jmethodID mid = FuncStat::env->GetMethodID(clazz, "getClass", "()Ljava/lang/Class;");
	jobject clsObj = FuncStat::env->CallObjectMethod(object, mid);
	jclass clazzz = FuncStat::env->GetObjectClass(clsObj);
	mid = FuncStat::env->GetMethodID(clazzz, "getCanonicalName", "()Ljava/lang/String;");
	jstring strObj = (jstring)FuncStat::env->CallObjectMethod(clsObj, mid);

	const char* str = FuncStat::env->GetStringUTFChars(strObj, nullptr);
	std::string res(str);

	FuncStat::env->ReleaseStringUTFChars(strObj, str);

	for (char& c : res)
	{
		if (c == '.')
		{
			c = '/';
		}
	}

	objects[res] = FuncStat::env->NewGlobalRef(object);
}

void addFunction(jstring name, jstring signature, int rtn, int argc, lua_State* l)
{
	const char* str = FuncStat::env->GetStringUTFChars(name, nullptr);
	const char* str2 = FuncStat::env->GetStringUTFChars(signature, nullptr);

	lua_newtable(l);
	lua_pushinteger(l, funcs.size());
	lua_setfield(l, -2, "id");
	luaL_getmetatable(l, "jfunc");
	lua_setmetatable(l, -2);

	if (inClass)
		lua_setfield(l, -2, str);
	else
		lua_setglobal(l, str);

	funcs.push_back({});
	JFunc2& fun = funcs[funcs.size() - 1];

	fun.init(str, str2, rtn, argc);

	FuncStat::env->ReleaseStringUTFChars(name, str);
	FuncStat::env->ReleaseStringUTFChars(signature, str2);
}

void deleteRefs()
{
	for (auto& [k, v] : objects)
	{
		FuncStat::env->DeleteGlobalRef(v);
	}
	objects.clear();
}
