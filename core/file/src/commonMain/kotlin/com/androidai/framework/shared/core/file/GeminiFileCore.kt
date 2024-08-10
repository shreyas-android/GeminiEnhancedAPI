package com.androidai.framework.shared.core.file

object GeminiFileCore {

    var apiKey:String = ""
    var isDebug:Boolean = false

    fun init(isDebug:Boolean, apiKey:String){
        GeminiFileCore.apiKey = apiKey
        GeminiFileCore.isDebug = isDebug
    }
}