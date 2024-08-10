package com.androidai.framework.shared.core.model.data.enums

import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse

sealed class GeminiAIGenerate {

    object Generating: GeminiAIGenerate()

    data class AllFileUploaded(val files:List<GeminiFileResponse>): GeminiAIGenerate()
     object FileUploading: GeminiAIGenerate()

    data class FileUploaded(val fileCount : Int, val totalCount:Int, val message:String): GeminiAIGenerate()

    data class StreamContentSuccess(val text:String): GeminiAIGenerate()

    data class StreamContentError(val message:String): GeminiAIGenerate()
}