package com.androidai.framework.shared.core.file

object GeminiFileCore {

    var apiKey:String = ""
    var isDebug:Boolean = false

    fun init(isDebug:Boolean, apiKey:String){
        com.androidai.framework.shared.core.file.GeminiFileCore.apiKey = apiKey
        com.androidai.framework.shared.core.file.GeminiFileCore.isDebug = isDebug
    }
}