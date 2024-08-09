package com.androidai.framework.shared.core.file.remote.remoteservice

import com.cogniheroid.framework.shared.core.geminifile.data.request.GeminiFileUploadRequest
import com.cogniheroid.framework.shared.core.geminifile.repository.RemoteConstants
import com.cogniheroid.framework.shared.core.geminifile.utils.appendConfiguration
import com.cogniheroid.framework.shared.core.geminifile.remote.path.RemoteUrlPath
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

internal class FileRemoteService(
        private val remoteUrlPath: RemoteUrlPath,
        private val apiKey:String,
        private val httpClient: HttpClient
) {

    suspend fun uploadAttachment(
            geminiFileUploadRequest : GeminiFileUploadRequest,
            onFileUploadListener : com.androidai.framework.shared.core.file.callback.OnFileUploadListener??
    ): HttpResponse {

        return httpClient.submitFormWithBinaryData(
                url = remoteUrlPath.uploadFileUrl(),
                formData = formData {
                    append(
                        RemoteConstants.REMOTE_QUERY_ATTACH_FILE, geminiFileUploadRequest.fileData,  Headers.build {
                            append(HttpHeaders.ContentType, geminiFileUploadRequest.contentType)
                            append(HttpHeaders.ContentDisposition, "filename=\"${geminiFileUploadRequest.fileName}\"")
                        })
                }
        ) {
            appendConfiguration(apiKey)
            onUpload { bytesSentTotal, contentLength ->
                onFileUploadListener?.onUpload(bytesSentTotal, contentLength)
            }
        }
    }

    suspend fun getFiles(pageSize:Int, pageToken:String):HttpResponse{
        return httpClient.get(remoteUrlPath.getFilesUrl()) {
            url {
                parameters.append(RemoteConstants.REMOTE_QUERY_PAGE_SIZE, pageSize.toString())
                if(pageToken.isNotEmpty()) {
                    parameters.append(RemoteConstants.REMOTE_QUERY_PAGE_TOKEN, pageToken)
                }
            }
            appendConfiguration(apiKey)
        }
    }

    suspend fun getFileByName(fileName:String) : HttpResponse {
        return httpClient.get(remoteUrlPath.getFileByNameUrl(fileName)) {
            appendConfiguration(apiKey)
        }
    }

    suspend fun deleteFileByName(fileName : String):HttpResponse{
        return httpClient.delete(remoteUrlPath.deleteFileByNameUrl(fileName)) {
            appendConfiguration(apiKey)
        }
    }
}