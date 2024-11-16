package com.androidai.framework.shared.core.model.data.mapper

import com.androidai.framework.shared.core.model.data.model.GeminiAIGenerate
import com.androidai.framework.shared.core.model.data.model.GeminiAIGenerateWithFile

fun GeminiAIGenerate.toGeminiAIGenerateWithFile() : GeminiAIGenerateWithFile {
    return when(this){
        GeminiAIGenerate.Generating -> {
            GeminiAIGenerateWithFile.Generating
        }
        is GeminiAIGenerate.StreamContentError -> {
            GeminiAIGenerateWithFile.StreamContentError(this.message)
        }
        is GeminiAIGenerate.StreamContentSuccess -> {
            GeminiAIGenerateWithFile.StreamContentSuccess(this.text)
        }
    }
}