package com.androidai.framework.shared.core.file.repository

import com.cogniheroid.framework.shared.core.geminifile.data.request.GeminiFileUploadRequest
import com.cogniheroid.framework.shared.core.geminifile.data.response.file.GeminiFileListResponse
import com.cogniheroid.framework.shared.core.geminifile.data.response.file.GeminiFileResponse
import com.cogniheroid.framework.shared.core.geminifile.data.response.RemoteResponse
import com.cogniheroid.framework.shared.core.geminifile.data.response.file.GeminiFileUploadResponse
 interface FileRepository {

    companion object {
        fun getInstance(): FileRepository {
            return com.androidai.framework.shared.core.file.Instance.fileRepositoryImpl
        }
    }

    suspend fun uploadAttachment(
            geminiFileUploadRequest : GeminiFileUploadRequest,
            onFileUploadListener : com.androidai.framework.shared.core.file.callback.OnFileUploadListener?) : RemoteResponse<GeminiFileUploadResponse>

     suspend fun getFiles(pageSize:Int = 100, pageToken:String = "") : RemoteResponse<GeminiFileListResponse>

     suspend fun getFileByName(fileName:String): RemoteResponse<GeminiFileResponse>

     suspend fun deleteFileByName(fileName:String): RemoteResponse<Boolean>


}