#pragma once
#include <jni.h>
#include <string>
struct FuncStat
{
	static jobject obj;
	static jclass clazz;
	static JNIEnv* env;
	static std::string storageDir;
	static void setVals(JNIEnv* _env, jobject& _obj);
};

template <typename T, typename... Args> class JFunc
{
  public:
	JFunc()
	{
	}
	JFunc(const char* name, const char* sig)
	{
		init(name, sig);
	}
	void init(const char* name, const char* sig)
	{
		method = FuncStat::env->GetMethodID(FuncStat::clazz, name, sig);
    obj = FuncStat::obj;
	}
	void callV(Args... args)
	{
    FuncStat::env->CallVoidMethod(obj, method, args...);
	}
	bool callB(Args... args)
	{
		return FuncStat::env->CallBooleanMethod(obj, method, args...);
	}
	T call(Args... args)
	{
		return (T)FuncStat::env->CallObjectMethod(obj, method, args...);
	}

  private:
	jmethodID method;
  jobject obj;
};
