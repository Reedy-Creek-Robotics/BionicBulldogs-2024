#include "JFunc.hpp"
#include "LoadFunc.hpp"
#include <lua/lua.hpp>
#include <string>
#include <unordered_map>

lua_State *l = nullptr;
JFunc<void, jstring> printF;
JFunc<void, jstring> errorF;
JFunc<void, jstring> jniErrorF;

std::unordered_map<std::string, int> opmodes;
int dispMarkerInd = 0;

void print(const char *str) {
    jstring j = FuncStat::env->NewStringUTF(str);
    printF.callV(j);
    FuncStat::env->ReleaseStringUTFChars(j, FuncStat::env->GetStringUTFChars(j, nullptr));
}

void err(const char *str) {
    jstring j = FuncStat::env->NewStringUTF(str);
    errorF.callV(j);
}

void jniErr(std::string msg) {
    jstring j = FuncStat::env->NewStringUTF(msg.c_str());
    jniErrorF.callV(j);
}

std::string getPathName(const std::string &name) {
    int i = -1;
    for (auto &[k, v]: opmodes) {
        if (name == k) {
            i = v;
            break;
        }
    }
    if (i == -1)
        err(("opmodes table doesnt contain opmode " + name).c_str());
    lua_getglobal(l, "Opmodes");
    lua_rawgeti(l, -1, i);
    lua_getfield(l, -1, "path");
    if (lua_type(l, -1) == LUA_TSTRING) {
        std::string str = lua_tostring(l, -1);
        lua_pop(l, 3);
        return str;
    }
    lua_pop(l, 3);
    return "";
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_org_firstinspires_ftc_teamcode_modules_lua_Lua_internalInit(JNIEnv *env,
                                                                 jobject thiz, jobject stdlib) {
    jobject ref = env->NewGlobalRef(stdlib);
    addObject(ref);
    FuncStat::setVals(env, ref);
    printF.init("print", "(Ljava/lang/String;)V");
    errorF.init("err", "(Ljava/lang/String;)V");
    jniErrorF.init("jniErr", "(Ljava/lang/String;)V");

    JFunc<jstring> getDataDir("getDataDir", "()Ljava/lang/String;");

    jstring dataDirJ = getDataDir.call();
    const char *dataDirC = env->GetStringUTFChars(dataDirJ, nullptr);
    std::string dataDir = dataDirC;
    env->ReleaseStringUTFChars(dataDirJ, dataDirC);

    FuncStat::storageDir = dataDir;

    l = luaL_newstate();
    luaL_openlibs(l);

    luaL_dostring(l, ("package.path = package.path .. ';" + dataDir + "/lua/?.lua'").c_str());

    loadFuncs(l);

    if (luaL_dofile(l, (dataDir + "/lua/main.lua").c_str())) {
        err(lua_tostring(l, -1));
        return nullptr;
    }
    lua_getglobal(l, "Opmodes");
    if (lua_type(l, -1) != LUA_TTABLE) {
        err("opmodes table must be a table");
        return nullptr;
    }
    lua_pushnil(l);

    int count = 0;
    while (lua_next(l, -2)) {
        count++;
        lua_pushvalue(l, -2);
        std::string key = lua_tostring(l, -1);

        if (lua_type(l, -2) != LUA_TTABLE) {
            lua_pop(l, 2);
            continue;
        }

        lua_getfield(l, -2, "name");
        if (lua_type(l, -1) != LUA_TSTRING) {
            err(("name of opmode '" + key + "' must be a string").c_str());
            return nullptr;
        }
        std::string name = lua_tostring(l, -1);
        lua_pop(l, 1);
        opmodes.emplace(name, stoi(key));

        lua_pop(l, 2);
    }
    lua_pop(l, 1);

    jobjectArray arr = env->NewObjectArray(count, env->FindClass("java/lang/String"),
                                           env->NewStringUTF("T"));
    int i = 0;
    for (auto &[k, v]: opmodes) {
        env->SetObjectArrayElement(arr, i++, env->NewStringUTF(k.c_str()));
    }
    return arr;
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_modules_lua_Lua_start(JNIEnv *env, jobject thiz,
                                                          jstring name,
                                                          int recognition) {
    lua_settop(l, 0);
    lua_newtable(l);
    FuncStat::obj = thiz;
    lua_getglobal(l, "Opmodes");
    int ind = -1;
    const char *c = env->GetStringUTFChars(name, nullptr);
    for (auto &[k, v]: opmodes) {
        if (k == c) {
            ind = v;
            break;
        }
    }
    if (ind == -1)
        err(("opmodes table doesnt contain opmode " + std::string(c)).c_str());

    lua_rawgeti(l, -1, ind);
    lua_getfield(l, -1, "start");
    if (lua_type(l, -1) == LUA_TFUNCTION) {
        lua_pushvalue(l, 1);
        lua_pushinteger(l, recognition);
        if (lua_pcall(l, 2, 0, 0)) {
            err(lua_tostring(l, -1));
            return;
        }
    }
    lua_settop(l, 1);
    lua_getglobal(l, "Opmodes");
    lua_rawgeti(l, -1, ind);
    lua_getfield(l, -1, "markers");
    if (lua_type(l, -1) != LUA_TTABLE) {
        lua_pop(l, 1);
        lua_newtable(l);
    }
    dispMarkerInd = 0;
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_modules_lua_Lua_update(JNIEnv *env, jobject thiz,
                                                           float deltaTime, float elapsedTime) {
    lua_getfield(l, -2, "update");
    if (lua_isfunction(l, -1)) {
        lua_pushvalue(l, 1);
        lua_newtable(l);
        lua_pushnumber(l, deltaTime);
        lua_setfield(l, -2, "delta");
        lua_pushnumber(l, elapsedTime);
        lua_setfield(l, -2, "elapsed");
        if (lua_pcall(l, 2, 0, 0)) {
            err(lua_tostring(l, -1));
            return;
        }
    }
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_modules_lua_Lua_stop(JNIEnv *env, jobject thiz) {
    lua_close(l);
    l = nullptr;
}

void callNextDispMarker(std::string str) {
    if (str == "") {
        dispMarkerInd++;
        lua_rawgeti(l, 4, dispMarkerInd);
        if (lua_type(l, -1) == LUA_TFUNCTION) {
            lua_pushvalue(l, 1);
            if (lua_pcall(l, 1, 0, 0)) {
                err(lua_tostring(l, -1));
                return;
            }
        }
    } else {
        lua_getfield(l, 4, str.c_str());
        if (lua_type(l, -1) == LUA_TFUNCTION) {
            lua_pushvalue(l, 1);
            if (lua_pcall(l, 1, 0, 0)) {
                err(lua_tostring(l, -1));
                return;
            }
        }
    }
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_modules_lua_Lua_addObject(JNIEnv *env,
                                                              jobject thiz,
                                                              jobject thing) {
    FuncStat::setVals(env, thiz);
    if (l != nullptr) {
        deleteRefs();
        lua_close(l);
    }
    addObject(thing);
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_modules_lua_Lua_addFunction(JNIEnv *env, jobject thiz,
                                                                jobject other, jstring name,
                                                                jstring signature) {
    FuncStat::setVals(env, thiz);
    if (l != nullptr) {
        deleteRefs();
        lua_close(l);
    }
    addObject(other);
    addFunction(name, signature);
}
