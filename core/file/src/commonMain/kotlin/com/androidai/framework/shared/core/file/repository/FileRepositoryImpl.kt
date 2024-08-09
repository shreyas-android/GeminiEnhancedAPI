package com.androidai.framework.shared.core.file.repository

import com.cogniheroid.framework.shared.core.geminifile.data.request.GeminiFileUploadRequest
import com.cogniheroid.framework.shared.core.geminifile.utils.remoteSafeCall
import com.cogniheroid.framework.shared.core.geminifile.remote.remoteservice.FileRemoteService
import com.cogniheroid.framework.shared.core.geminifile.data.response.file.GeminiFileListResponse
import com.cogniheroid.framework.shared.core.geminifile.data.response.file.GeminiFileResponse
import com.cogniheroid.framework.shared.core.geminifile.data.response.RemoteResponse
import com.cogniheroid.framework.shared.core.geminifile.data.response.file.GeminiFileUploadResponse
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText

internal class FileRepositoryImpl(
        private val fileRemoteService : FileRemoteService) : FileRepository {

    override suspend fun uploadAttachment(
            geminiFileUploadRequest : GeminiFileUploadRequest,
            onFileUploadListener : com.androidai.framework.shared.core.file.callback.OnFileUploadListener?) : RemoteResponse<GeminiFileUploadResponse> {
        return remoteSafeCall(action = {
            fileRemoteService.
            uploadAttachment(geminiFileUploadRequest, onFileUploadListener)
        }, onSuccess = {
            it.body() }, onLogFailure = {})
    }

    override suspend fun getFiles(pageSize:Int, pageToken:String) : RemoteResponse<GeminiFileListResponse> {
        return remoteSafeCall(action = {
            fileRemoteService.getFiles(pageSize, pageToken)
        }, onSuccess = {
            it.body() }, onLogFailure = {})
    }

    override suspend fun getFileByName(fileName : String) : RemoteResponse<GeminiFileResponse> {
        return remoteSafeCall(action = {
            fileRemoteService.getFileByName(fileName)
        }, onSuccess = {
            it.body() }, onLogFailure = {})
    }

    override suspend fun deleteFileByName(fileName : String) : RemoteResponse<Boolean> {
        return remoteSafeCall(action = {
            fileRemoteService.deleteFileByName(fileName)
        }, onSuccess = {
            it.bodyAsText().isNotEmpty() }, onLogFailure = {})
    }

}