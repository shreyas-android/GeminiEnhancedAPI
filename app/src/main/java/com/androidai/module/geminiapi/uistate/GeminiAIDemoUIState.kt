package com.androidai.module.geminiapi.uistate

import com.androidai.module.geminiapi.utils.FileUriInfo

data class GeminiAIDemoUIState(
        val inputText:String,
        val outputText:String,
        val isGenerating:Boolean,
        val fileUriInfoItems: List<FileUriInfo>,
)