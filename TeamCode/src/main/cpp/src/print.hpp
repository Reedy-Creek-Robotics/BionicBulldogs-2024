#include <string>
#include <jni.h>

void err(const std::string& msg);
void print(const std::string& msg);
void jniErr(const std::string& msg);

void initFuncs(jobject obj, JNIEnv* env);
