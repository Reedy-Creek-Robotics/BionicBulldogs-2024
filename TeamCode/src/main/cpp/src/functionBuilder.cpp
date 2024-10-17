#include "functionBuilder.hpp"

#include <cstring>
#include <vector>

#include "print.hpp"
#include "utils.hpp"

#define setCurrentObject Java_org_firstinspires_ftc_teamcode_opmodeloader_FunctionBuilder_setCurrentObject
#define addFunction Java_org_firstinspires_ftc_teamcode_opmodeloader_FunctionBuilder_addFunction

#define LUA_TINT 8

static lua_State* l;

static JNIEnv* env;

struct Function
{
	int objectId;
	jmethodID function;

	char rtnType;
	std::vector<char> argTypes;
};

std::vector<Function> functions;

std::vector<jobject> objects;

int currentObjectId;

void setEnv(JNIEnv* env2)
{
	env = env2;
}

int callFun(lua_State* l);

void initFunctionBuilder(JNIEnv* env, lua_State* L)
{
	print("nuke list");
  l = L;
	for (jobject object : objects)
	{
		env->DeleteGlobalRef(object);
	}
	objects.clear();
	functions.clear();

	luaL_newmetatable(l, "functionBuilder");
	lua_pushcfunction(l, callFun);
	lua_setfield(l, -2, "__call");
}

int callFun(lua_State* l)
{
	int argc = lua_gettop(l) - 1;
	lua_getfield(l, 1, "id");

	int i = lua_tointeger(l, -1);
	lua_pop(l, 1);

	Function& fun = functions[i];

	if (fun.argTypes.size() != argc)
	{
		luaL_error(l, "expected %i args, got %i", fun.argTypes.size(), argc);
	}

	jvalue* args = nullptr;
	if (argc > 0)
	{
		args = new jvalue[argc];

		for (int i = 0; i < argc; i++)
		{
			int type = lua_type(l, i + 2);
			if (type != fun.argTypes[i])
			{
				const char* msg;
				const char* typearg; /* name for the type of the actual argument */
				if (luaL_getmetafield(l, i + 1, "__name") == LUA_TSTRING)
					typearg = lua_tostring(l, -1); /* use the given type name */
				else if (lua_type(l, i + 1) == LUA_TLIGHTUSERDATA)
					typearg = "light userdata"; /* special name for messages */
				else
					typearg = luaL_typename(l, i + 1); /* standard name */
				msg = lua_pushfstring(l, "%s expected, got %s", luaL_typename(l, fun.argTypes[i]), typearg);
				luaL_argerror(l, i + 1, msg);
				return 0;
			}
			switch (type)
			{
			case LUA_TNUMBER:
				args[i].d = lua_tonumber(l, i + 2);
				break;
			case LUA_TBOOLEAN:
				args[i].z = lua_toboolean(l, i + 2);
				break;
			case LUA_TSTRING: {
				const char* str = lua_tostring(l, i + 2);
				args[i].l = env->NewStringUTF(str);
				break;
			}
			default:
				break;
			}
		}
	}

  jobject object = objects[fun.objectId];

	switch (fun.rtnType)
	{
	case LUA_TNONE: {
		bool rtn = env->CallBooleanMethod(object, fun.function, args);
		if (args)
		{
			delete[] args;
		}
		if (rtn)
		{
			luaL_error(l, "opmode stopped :)");
		}
		return 0;
	}
	case LUA_TNIL: {
		env->CallVoidMethod(object, fun.function, args);
		if (args)
		{
			delete[] args;
		}
		return 0;
	}
	case LUA_TNUMBER: {
		float rtn = env->CallDoubleMethod(object, fun.function, args);
		lua_pushnumber(l, rtn);
		if (args)
		{
			delete[] args;
		}
		return 1;
	}
	case LUA_TBOOLEAN: {
		bool rtn = env->CallBooleanMethod(object, fun.function, args);
		lua_pushboolean(l, rtn);
		if (args)
		{
			delete[] args;
		}
		return 1;
	}
	case LUA_TSTRING:
		jstring rtn = (jstring)env->CallObjectMethod(object, fun.function, args);
		const char* str = env->GetStringUTFChars(rtn, nullptr);
		lua_pushstring(l, str);
		env->ReleaseStringUTFChars(rtn, str);
		if (args)
		{
			delete[] args;
		}
		return 1;
	}
	return 0;
}

extern "C" JNIEXPORT void JNICALL setCurrentObject(JNIEnv* env, jobject thiz, jobject object)
{
  print("add to list");
	currentObjectId = objects.size();
  jobject obj = env->NewGlobalRef(object);
	objects.push_back(obj);
}

extern "C" JNIEXPORT void JNICALL addFunction(JNIEnv* env, jobject thiz, jstring name, jstring sig, int rtnType,
											  int argc)
{
	jclass clazz = env->GetObjectClass(objects[currentObjectId]);

	const char* nameStr = env->GetStringUTFChars(name, nullptr);
	const char* signatureStr = env->GetStringUTFChars(sig, nullptr);

	jmethodID methodId = env->GetMethodID(clazz, nameStr, signatureStr);

	if (env->ExceptionCheck())
	{
		env->ExceptionDescribe();
		env->ExceptionClear();

		std::string msg = format("could not find function '%s' with signature '%s'", nameStr, signatureStr);

		jniErr(msg);
	}

	int funId = functions.size();
	functions.push_back({});

	Function& fun = functions[functions.size() - 1];

	int len = strlen(signatureStr);

	bool end = false;

	fun.objectId = currentObjectId;

	fun.function = methodId;

	fun.rtnType = rtnType;

	for (int i = 0; i < len; i++)
	{
		char c = signatureStr[i];
		switch (c)
		{
		case ')':
			end = true;
			break;
		case 'Z':
			fun.argTypes.push_back(LUA_TBOOLEAN);
			break;
		case 'L':
			fun.argTypes.push_back(LUA_TSTRING);
			break;
		case 'I':
			fun.argTypes.push_back(LUA_TINT);
			break;
		case 'D':
			fun.argTypes.push_back(LUA_TNUMBER);
			break;
		default:
			break;
		}
		if (end)
			break;
	}

	lua_newtable(l);
	lua_pushnumber(l, funId);
	lua_setfield(l, -2, "id");
	luaL_getmetatable(l, "functionBuilder");
	lua_setmetatable(l, -2);
	lua_setglobal(l, nameStr);

	env->ReleaseStringUTFChars(name, nameStr);
	env->ReleaseStringUTFChars(sig, signatureStr);
}
