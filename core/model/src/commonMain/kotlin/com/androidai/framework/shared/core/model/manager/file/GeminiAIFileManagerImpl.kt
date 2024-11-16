package com.androidai.framework.shared.core.model.manager.file

import com.androidai.framework.shared.core.model.callback.OnFileUploadListener
import com.androidai.framework.shared.core.file.callback.FileUploadCallback
import com.androidai.framework.shared.core.file.data.request.GeminiFileUploadRequest
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.file.repository.FileRepository
import com.androidai.framework.shared.core.model.data.model.GeminiAIFileState
import com.androidai.framework.shared.core.model.data.model.UploadFileInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class GeminiAIFileManagerImpl(private val geminiFileRepository : FileRepository):GeminiAIFileManager {

    private val managerScope = CoroutineScope(Dispatchers.Default)

   override suspend fun uploadFiles(
           uploadFileInfoList : List<UploadFileInfo>,
           onFileUploadListener : OnFileUploadListener?) : Flow<GeminiAIFileState> {

        val fileUploadFlow:MutableStateFlow<GeminiAIFileState> = MutableStateFlow(
            GeminiAIFileState.FilesUploading)
        val files = mutableListOf<GeminiFileResponse>()

        val totalFilesCount = uploadFileInfoList.size
        var fileUploaded = 0

        managerScope.launch {
            uploadFileInfoList.forEach {
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
                                GeminiAIFileState.FileUploaded(fileUploaded, totalFilesCount, "")
                            files.add(response.data.file)
                        }
                    }
                }.await()
            }

            fileUploadFlow.value = GeminiAIFileState.AllFileUploaded(files)
        }
        return fileUploadFlow

    }

    override  suspend fun getFiles(pageSize:Int, pageToken:String) : RemoteResponse<GeminiFileListResponse>{
       return geminiFileRepository.getFiles(pageSize, pageToken)
    }

    override suspend fun getFileByName(fileName:String): RemoteResponse<GeminiFileResponse>{
        return geminiFileRepository.getFileByName(fileName)
    }

    override  suspend fun deleteFileByName(fileName:String): RemoteResponse<Boolean>{
        return geminiFileRepository.deleteFileByName(fileName)
    }

}