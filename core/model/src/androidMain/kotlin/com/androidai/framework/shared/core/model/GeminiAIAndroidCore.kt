package com.androidai.framework.shared.core.model

import com.androidai.framework.shared.core.model.manager.file.GeminiAIFileManagerImpl
import com.androidai.framework.shared.core.file.repository.FileRepository
import com.androidai.framework.shared.core.model.manager.GeminiAIManagerImpl
import com.androidai.framework.shared.core.model.manager.GeminiAIManager
import com.androidai.framework.shared.core.model.data.enums.GeminiAIModel

object GeminiAIAndroidCore {

    private var apiKey:String = ""
    private var modelName:GeminiAIModel = GeminiAIModel.GEMINI_PRO
    private var isDebug:Boolean = false

    private var geminiAIManagerImpl: GeminiAIManager? = null

    fun init(isDebug:Boolean, apiKey:String, modelName : GeminiAIModel){
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

    private fun createGeminiManagerInstance(apiKey:String, modelName:GeminiAIModel) : GeminiAIManagerImpl {
        val geminiAIFileManagerImpl = GeminiAIFileManagerImpl(FileRepository.getInstance())
        return GeminiAIManagerImpl(apiKey,modelName,
            geminiAIFileManagerImpl)
    }
}