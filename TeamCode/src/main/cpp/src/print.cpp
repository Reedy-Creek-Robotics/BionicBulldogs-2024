#include "print.hpp"

jmethodID printId;
jmethodID errId;
jmethodID jniErrId;

jobject object = nullptr;
JNIEnv* env;

void print(const std::string& msg)
{
	jobject arg = env->NewStringUTF(msg.c_str());
	env->CallVoidMethod(object, printId, arg);
  env->DeleteLocalRef(arg);
}

void err(const std::string& msg)
{
	jobject arg = env->NewStringUTF(msg.c_str());
	env->CallVoidMethod(object, errId, arg);
  env->DeleteLocalRef(arg);
}

void jniErr(const std::string& msg)
{
	jobject arg = env->NewStringUTF(msg.c_str());
	env->CallVoidMethod(object, jniErrId, arg);
  env->DeleteLocalRef(arg);
}

void initFuncs(jobject obj, JNIEnv* e)
{
  env = e;
  if(object)
    env->DeleteGlobalRef(object);
  object = env->NewGlobalRef(obj);
  jclass clazz = env->GetObjectClass(obj);
  printId = env->GetMethodID(clazz, "print", "(Ljava/lang/String;)V");
  errId = env->GetMethodID(clazz, "err", "(Ljava/lang/String;)V");
  jniErrId = env->GetMethodID(clazz, "jniErr", "(Ljava/lang/String;)V");
}
