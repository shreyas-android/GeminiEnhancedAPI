package com.androidai.framework.shared.core.model.manager

import com.androidai.framework.shared.core.model.callback.OnFileUploadListener
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.model.data.enums.GeminiAIGenerate

interface GeminiAIManager {
    suspend fun generateTextStreamContent(modelInputList : List<ModelInput>,
                                          onFileUploadListener : OnFileUploadListener?,
                                          defaultErrorMessage : String): com.androidai.framework.shared.core.model.flow.CommonFlow<GeminiAIGenerate>

    suspend fun getFiles(pageSize:Int = 100, pageToken:String = "") : RemoteResponse<GeminiFileListResponse>

    suspend fun getFileByName(fileName:String): RemoteResponse<GeminiFileResponse>

    suspend fun deleteFileByName(fileName:String): RemoteResponse<Boolean>
}