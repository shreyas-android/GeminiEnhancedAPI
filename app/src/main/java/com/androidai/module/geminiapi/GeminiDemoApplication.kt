package com.androidai.module.geminiapi

import android.app.Application
import com.androidai.framework.shared.core.model.GeminiAIAndroidCore
import com.androidai.framework.shared.core.model.data.enums.GeminiAIModel

class GeminiDemoApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        GeminiAIAndroidCore.init(BuildConfig.DEBUG,
            BuildConfig.android_ai_gemini_api_key, GeminiAIModel.GEMINI_PRO.modelName)
    }
}