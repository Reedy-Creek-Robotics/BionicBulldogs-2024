#include <string>
#include <jni.h>

#include "utils.hpp"

void err(const std::string& msg);
void print(const std::string& msg);
void jniErr(const std::string& msg);

template<typename ...Args> void printf(const char* fmt, Args ...args)
{
  print(format(fmt, args...));
}

template<typename ...Args> void errf(const char* fmt, Args ...args)
{
  err(format(fmt, args...));
}

template<typename ...Args> void jniErrf(const char* fmt, Args ...args)
{
  jniErr(format(fmt, args...));
}

void initFuncs(jobject obj, JNIEnv* env);
