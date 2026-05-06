#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "SoundGuardNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT void JNICALL
Java_com_mohamedashraf_soundguardpro_MainActivity_maximizeVolumeNative(
        JNIEnv* env,
        jobject /* this */,
        jobject audioManager) {
    
    jclass audioManagerClass = env->GetObjectClass(audioManager);
    jmethodID setStreamVolumeMethod = env->GetMethodID(audioManagerClass, "setStreamVolume", "(III)V");
    jmethodID getStreamMaxVolumeMethod = env->GetMethodID(audioManagerClass, "getStreamMaxVolume", "(I)I");

    // STREAM_RING = 2
    int streamType = 2; 
    int maxVolume = env->CallIntMethod(audioManager, getStreamMaxVolumeMethod, streamType);
    
    LOGI("Setting volume to max: %d", maxVolume);
    
    // Flag: SHOW_UI = 1
    env->CallVoidMethod(audioManager, setStreamVolumeMethod, streamType, maxVolume, 1);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_mohamedashraf_soundguardpro_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "SoundGuard Pro Engine Active";
    return env->NewStringUTF(hello.c_str());
}
