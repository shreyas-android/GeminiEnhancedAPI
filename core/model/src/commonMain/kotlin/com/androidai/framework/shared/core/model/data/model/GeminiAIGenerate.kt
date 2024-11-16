package com.androidai.framework.shared.core.model.data.model

import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse

sealed class GeminiAIGenerate {

    object Generating: GeminiAIGenerate()

    data class StreamContentSuccess(val text:String): GeminiAIGenerate()

    data class StreamContentError(val message:String): GeminiAIGenerate()

}

sealed class GeminiAIGenerateWithFile {

    object Generating: GeminiAIGenerateWithFile()

    data class AllFileUploaded(val files:List<GeminiFileResponse>): GeminiAIGenerateWithFile()

    object FilesUploadFailed: GeminiAIGenerateWithFile()

    object FilesUploading: GeminiAIGenerateWithFile()

    data class FileUploaded(val fileCount : Int, val totalCount:Int, val message:String): GeminiAIGenerateWithFile()


    data class StreamContentSuccess(val text:String): GeminiAIGenerateWithFile()

    data class StreamContentError(val message:String): GeminiAIGenerateWithFile()

}