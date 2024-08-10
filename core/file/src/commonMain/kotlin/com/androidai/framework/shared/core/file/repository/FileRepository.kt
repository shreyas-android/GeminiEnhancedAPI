package com.androidai.framework.shared.core.file.repository

import com.androidai.framework.shared.core.file.data.request.GeminiFileUploadRequest
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileUploadResponse

interface FileRepository {

    companion object {
        fun getInstance(): FileRepository {
            return com.androidai.framework.shared.core.file.Instance.fileRepositoryImpl
        }
    }

    suspend fun uploadAttachment(
            geminiFileUploadRequest : GeminiFileUploadRequest,
            fileUploadCallback : com.androidai.framework.shared.core.file.callback.FileUploadCallback?) : RemoteResponse<GeminiFileUploadResponse>

     suspend fun getFiles(pageSize:Int = 100, pageToken:String = "") : RemoteResponse<GeminiFileListResponse>

     suspend fun getFileByName(fileName:String): RemoteResponse<GeminiFileResponse>

     suspend fun deleteFileByName(fileName:String): RemoteResponse<Boolean>


}