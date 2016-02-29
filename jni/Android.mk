LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := rlv2
LOCAL_SRC_FILES := rlv2.cpp

include $(BUILD_SHARED_LIBRARY)
