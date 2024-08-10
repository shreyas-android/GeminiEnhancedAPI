package com.androidai.framework.shared.core.model

import com.androidai.framework.shared.core.model.manager.GeminiAIFileManager
import com.androidai.framework.shared.core.file.repository.FileRepository
import com.androidai.framework.shared.core.model.manager.GeminiAIAndroidManager
import com.androidai.framework.shared.core.model.manager.GeminiAIManager
import com.androidai.framework.shared.core.model.data.enums.GeminiAIModel

object GeminiAIAndroidCore {

    private var apiKey:String = ""
    private var modelName:String = GeminiAIModel.GEMINI_PRO.modelName
    private var isDebug:Boolean = false

    private var geminiAIManagerImpl: GeminiAIManager? = null

    fun init(isDebug:Boolean, apiKey:String, modelName : String){
        GeminiAIAndroidCore.apiKey = apiKey
        GeminiAIAndroidCore.modelName = modelName
        GeminiAIAndroidCore.isDebug = isDebug
        GeminiAICore.init(isDebug, apiKey)
        geminiAIManagerImpl = createGeminiManagerInstance(apiKey, modelName)
    }



    fun getGeminiAIManagerInstance() : GeminiAIManager {
        if(geminiAIManagerImpl == null){
            geminiAIManagerImpl = createGeminiManagerInstance(apiKey, modelName)
        }
        return geminiAIManagerImpl!!
    }

    private fun createGeminiManagerInstance(apiKey:String, modelName:String) : GeminiAIAndroidManager {
        val geminiAIFileManager = GeminiAIFileManager(FileRepository.getInstance())
        return GeminiAIAndroidManager(apiKey,modelName,
            geminiAIFileManager)
    }
}