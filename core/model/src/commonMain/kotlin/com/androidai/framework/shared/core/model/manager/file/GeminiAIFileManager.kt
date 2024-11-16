package com.androidai.framework.shared.core.model.manager.file

import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.model.callback.OnFileUploadListener
import com.androidai.framework.shared.core.model.data.model.GeminiAIFileState
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.model.data.model.UploadFileInfo
import kotlinx.coroutines.flow.Flow

interface GeminiAIFileManager {
    suspend fun uploadFiles(
            uploadFileInfoList : List<UploadFileInfo>,
            onFileUploadListener : OnFileUploadListener?) : Flow<GeminiAIFileState>

    suspend fun getFiles(pageSize:Int = 100, pageToken:String = "") : RemoteResponse<GeminiFileListResponse>

    suspend fun getFileByName(fileName:String): RemoteResponse<GeminiFileResponse>

    suspend fun deleteFileByName(fileName:String): RemoteResponse<Boolean>
}