package com.androidai.framework.shared.core.model.manager

import com.androidai.framework.shared.core.model.callback.OnFileUploadListener
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.model.flow.toCommonFlow
import com.androidai.framework.shared.core.file.callback.FileUploadCallback
import com.androidai.framework.shared.core.file.data.request.GeminiFileUploadRequest
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.file.repository.FileRepository
import com.androidai.framework.shared.core.model.data.enums.GeminiAIGenerate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class GeminiAIFileManager(private val geminiFileRepository : FileRepository) {

    private val managerScope = CoroutineScope(Dispatchers.Default)

    suspend fun uploadFiles(
            fileInputList : List<ModelInput.File>,
            onFileUploadListener : OnFileUploadListener?) : com.androidai.framework.shared.core.model.flow.CommonFlow<GeminiAIGenerate> {

        val fileUploadFlow:MutableStateFlow<GeminiAIGenerate> = MutableStateFlow(
            GeminiAIGenerate.FileUploading)
        val files = mutableListOf<GeminiFileResponse>()

        val totalFilesCount = fileInputList.size
        var fileUploaded = 0

        managerScope.launch {
            fileInputList.forEach {
                managerScope.async {

                    val response = geminiFileRepository.uploadAttachment(GeminiFileUploadRequest(
                        it.fileName, it.mimeType, it.data),
                        fileUploadCallback = object : FileUploadCallback {
                            override fun onUpload(bytesSentTotal : Long, contentLength : Long) {
                                onFileUploadListener?.onUploadFile(
                                    bytesSentTotal, contentLength, it)
                            }

                        })

                    when(response) {
                        is RemoteResponse.Error -> {

                        }

                        is RemoteResponse.Success -> {
                            fileUploaded += 1
                            fileUploadFlow.value =
                                GeminiAIGenerate.FileUploaded(fileUploaded, totalFilesCount, "")
                            files.add(response.data.file)
                        }
                    }
                }.await()
            }

            fileUploadFlow.value = GeminiAIGenerate.AllFileUploaded(files)
        }
        return fileUploadFlow.toCommonFlow()

    }

    suspend fun getFiles(pageSize:Int = 100, pageToken:String = "") : RemoteResponse<GeminiFileListResponse>{
       return geminiFileRepository.getFiles(pageSize, pageToken)
    }

    suspend fun getFileByName(fileName:String): RemoteResponse<GeminiFileResponse>{
        return geminiFileRepository.getFileByName(fileName)
    }

    suspend fun deleteFileByName(fileName:String): RemoteResponse<Boolean>{
        return geminiFileRepository.deleteFileByName(fileName)
    }

}