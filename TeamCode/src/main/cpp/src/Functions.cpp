#include "JFunc.hpp"
#include "LoadFunc.hpp"
#include <lua/lua.hpp>
#include <unordered_map>
#include <string>
#include "macros/Helpers.hpp"
#include "Lua.hpp"
#include <vector>

std::vector<JFunc2> funcs;

void errCheck() {
    JNIEnv *env = FuncStat::env;
    if (env->ExceptionCheck()) {
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

    if(fun.argc != argc)
    {
        luaL_error(l, ("expected " + std::to_string(fun.argc) + " args, got " + std::to_string(argc)).c_str());
    }

    jvalue* args = new jvalue[argc];

    for(int i = 0; i < argc; i++)
    {
        int type = lua_type(l, i + 1);
    }

    switch(fun.rtnType)
    {
        case LUA_TNONE:
            fun.callV(args);
            break;
        case LUA_TNUMBER:
            fun.callV(args);
            break;
        case LUA_TBOOLEAN:
            fun.callV(args);
            break;
    }
}

void loadFuncs(lua_State *l) {
    luaL_newmetatable(l, "jfunc");
    lua_pushcfunction(l, callJFunc);
}

void addObject(jobject object) {
    jclass clazz = FuncStat::env->GetObjectClass(object);
    jmethodID mid = FuncStat::env->GetMethodID(clazz, "getClass", "()Ljava/lang/Class;");
    jobject clsObj = FuncStat::env->CallObjectMethod(object, mid);
    jclass clazzz = FuncStat::env->GetObjectClass(clsObj);
    mid = FuncStat::env->GetMethodID(clazzz, "getCanonicalName", "()Ljava/lang/String;");
    jstring strObj = (jstring) FuncStat::env->CallObjectMethod(clsObj, mid);

    const char *str = FuncStat::env->GetStringUTFChars(strObj, nullptr);
    std::string res(str);

    FuncStat::env->ReleaseStringUTFChars(strObj, str);

    for (char &c: res) {
        if (c == '.') {
            c = '/';
        }
    }

    objects[res] = FuncStat::env->NewGlobalRef(object);
}

void addFunction(jstring name, jstring signature, lua_State *l) {
    const char *str = FuncStat::env->GetStringUTFChars(name, nullptr);
    const char *str2 = FuncStat::env->GetStringUTFChars(signature, nullptr);

    lua_newtable(l);

    funcs.push_back({});
    JFunc2& fun = funcs[funcs.size() - 1];

    fun.init(str, str2);

    FuncStat::env->ReleaseStringUTFChars(strObj, str);
}

void deleteRefs() {
    for (auto &[k, v]: objects) {
        FuncStat::env->DeleteGlobalRef(v);
    }
    objects.clear();
}
