package com.androidai.framework.shared.core.model.data.model

import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse


sealed class GeminiAIFileState {

    data class AllFileUploaded(val files:List<GeminiFileResponse>): GeminiAIFileState()

    object FilesUploadFailed: GeminiAIFileState()

    object FilesUploading: GeminiAIFileState()

    data class FileUploaded(val fileCount : Int, val totalCount:Int, val message:String): GeminiAIFileState()
}