package com.androidai.framework.shared.core.model

import com.androidai.framework.shared.core.file.GeminiFileCore

internal object GeminiAICore {
    fun init(isDebug:Boolean, apiKey:String){
        GeminiFileCore.init(isDebug, apiKey)

    }
}