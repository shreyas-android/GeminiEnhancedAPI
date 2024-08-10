package com.androidai.framework.shared.core.file.repository

import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.request.GeminiFileUploadRequest
import com.androidai.framework.shared.core.file.utils.remoteSafeCall
import com.androidai.framework.shared.core.file.remote.remoteservice.FileRemoteService
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileUploadResponse
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText

internal class FileRepositoryImpl(
        private val fileRemoteService : FileRemoteService) : FileRepository {

    override suspend fun uploadAttachment(
            geminiFileUploadRequest : GeminiFileUploadRequest,
            fileUploadCallback : com.androidai.framework.shared.core.file.callback.FileUploadCallback?) : RemoteResponse<GeminiFileUploadResponse> {
        return remoteSafeCall(action = {
            fileRemoteService.
            uploadAttachment(geminiFileUploadRequest, fileUploadCallback)
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