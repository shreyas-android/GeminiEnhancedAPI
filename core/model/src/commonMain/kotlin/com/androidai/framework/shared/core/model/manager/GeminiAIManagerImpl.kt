package com.androidai.framework.shared.core.model.manager

import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.model.callback.OnFileUploadListener
import com.androidai.framework.shared.core.model.data.enums.GeminiAIModel
import com.androidai.framework.shared.core.model.data.mapper.toGeminiAIGenerateWithFile
import com.androidai.framework.shared.core.model.data.model.GeminiAIFileState
import com.androidai.framework.shared.core.model.data.model.GeminiAIGenerate
import com.androidai.framework.shared.core.model.data.model.GeminiAIGenerateWithFile
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.model.data.model.UploadFileInfo
import com.androidai.framework.shared.core.model.data.model.toGeminiModelListInfo
import com.androidai.framework.shared.core.model.manager.file.GeminiAIFileManager
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.Content
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class GeminiAIManagerImpl(
        apiKey : String, geminiAIModel : GeminiAIModel,
        private val geminiAIFileManager : GeminiAIFileManager) : GeminiAIManager {

    private val managerScope = CoroutineScope(Dispatchers.Default)

    private val generativeModel = GenerativeModel(
        modelName = geminiAIModel.modelName,
        apiKey = apiKey,
    )

    override suspend fun generateTextContent(
            modelInputList : List<ModelInput>, uploadFileInfoList : List<UploadFileInfo>,
            onFileUploadListener : OnFileUploadListener?,
            defaultErrorMessage : String) : Flow<GeminiAIGenerateWithFile> {

        val contentFlow : MutableStateFlow<GeminiAIGenerateWithFile> =
            MutableStateFlow(GeminiAIGenerateWithFile.Generating)

        val filesUploadFlow =
            geminiAIFileManager.uploadFiles(uploadFileInfoList, onFileUploadListener)

        managerScope.launch {
            filesUploadFlow.collectLatest {
                when(it) {
                    is GeminiAIFileState.AllFileUploaded -> {
                        val newFileResponse = onHandleFileProcessing(
                            1, it.files)

                        if(newFileResponse == null) {
                            contentFlow.value = GeminiAIGenerateWithFile.FilesUploadFailed
                        } else {
                            contentFlow.value = GeminiAIGenerateWithFile.Generating
                            val tempModelInputList = modelInputList.toMutableList()
                            newFileResponse.forEach { fileResponse ->
                                tempModelInputList.add(
                                    ModelInput.File(
                                        isUser = false, fileResponse.name, fileResponse.mimeType,
                                        fileResponse.uri))
                            }

                            contentFlow.value = generateTextContent(
                                tempModelInputList,
                                defaultErrorMessage).toGeminiAIGenerateWithFile()
                        }

                    }

                    GeminiAIFileState.FilesUploadFailed -> {
                        contentFlow.value = GeminiAIGenerateWithFile.FilesUploadFailed
                    }

                    is GeminiAIFileState.FileUploaded -> {
                        contentFlow.value = GeminiAIGenerateWithFile.FileUploaded(
                            it.fileCount, it.totalCount, it.message)
                    }

                    GeminiAIFileState.FilesUploading -> {
                        contentFlow.value = GeminiAIGenerateWithFile.FilesUploading
                    }
                }
            }
        }

        return contentFlow
    }

    override suspend fun generateTextContentStream(
            modelInputList : List<ModelInput>, uploadFileInfoList : List<UploadFileInfo>,
            onFileUploadListener : OnFileUploadListener?,
            defaultErrorMessage : String) : Flow<GeminiAIGenerateWithFile> {
        val contentFlow : MutableStateFlow<GeminiAIGenerateWithFile> =
            MutableStateFlow(GeminiAIGenerateWithFile.Generating)

        val filesUploadFlow =
            geminiAIFileManager.uploadFiles(uploadFileInfoList, onFileUploadListener)

        managerScope.launch {
            filesUploadFlow.collectLatest {
                when(it) {
                    is GeminiAIFileState.AllFileUploaded -> {
                        val newFileResponse = onHandleFileProcessing(
                            1, it.files)

                        if(newFileResponse == null) {
                            contentFlow.value = GeminiAIGenerateWithFile.FilesUploadFailed
                        } else {
                            contentFlow.value = GeminiAIGenerateWithFile.Generating
                            val tempModelInputList = modelInputList.toMutableList()
                            newFileResponse.forEach { fileResponse ->
                                tempModelInputList.add(
                                    ModelInput.File(
                                        isUser = false, fileResponse.name, fileResponse.mimeType,
                                        fileResponse.uri))
                            }

                            launch {
                                generateTextContentStream(
                                    tempModelInputList,
                                    defaultErrorMessage).collectLatest { geminiAIGenerate ->
                                    contentFlow.value =
                                        geminiAIGenerate.toGeminiAIGenerateWithFile()
                                }
                            }

                        }

                    }

                    GeminiAIFileState.FilesUploadFailed -> {
                        contentFlow.value = GeminiAIGenerateWithFile.FilesUploadFailed
                    }

                    is GeminiAIFileState.FileUploaded -> {
                        contentFlow.value = GeminiAIGenerateWithFile.FileUploaded(
                            it.fileCount, it.totalCount, it.message)
                    }

                    GeminiAIFileState.FilesUploading -> {
                        contentFlow.value = GeminiAIGenerateWithFile.FilesUploading
                    }
                }
            }
        }

        return contentFlow
    }

    private suspend fun onHandleFileProcessing(
            retryCount : Int,
            filesResponse : List<GeminiFileResponse>,
    ) : List<GeminiFileResponse>? {

        if(retryCount != 5) {
            val isProcessing = filesResponse.any {
                it.state == "PROCESSING"
            }
            val activeFiles = filesResponse.filter {
                it.state == "ACTIVE"
            }

            if(isProcessing) { // contentFlow.value = GeminiAIGenerate.FileProcessing
                delay(10000)

                val newFileResponse = arrayListOf<GeminiFileResponse>()
                filesResponse.forEach {
                    when(val response = geminiAIFileManager.getFileByName(it.name)) {
                        is RemoteResponse.Error -> {

                        }

                        is RemoteResponse.Success -> {
                            newFileResponse.add(response.data)
                        }
                    }
                }
                onHandleFileProcessing(
                    retryCount + 1, newFileResponse)
            } else {
                return filesResponse
            }
        } else {
            return null
        }

        return null
    }

    override suspend fun generateTextContent(
            modelInputList : List<ModelInput>, defaultErrorMessage : String) : GeminiAIGenerate {
        val input = getGeminiContent(modelInputList)

        return runCatching {
            generativeModel.generateContent(input)
        }.fold(onSuccess = { generateContentResponse ->
            if(generateContentResponse.candidates.isNotEmpty()) {
                if(generateContentResponse.text.isNullOrEmpty()) {
                    GeminiAIGenerate.StreamContentError(defaultErrorMessage)
                } else {
                    GeminiAIGenerate.StreamContentSuccess(
                        generateContentResponse.text!!)
                }
            } else {
                GeminiAIGenerate.StreamContentError(defaultErrorMessage)
            }

        }, onFailure = { throwable ->
            GeminiAIGenerate.StreamContentError(
                throwable.message
                    ?: defaultErrorMessage)
        })
    }

    override suspend fun generateTextContentStream(
            modelInputList : List<ModelInput>,
            defaultErrorMessage : String) : Flow<GeminiAIGenerate> {
        val contentFlow = MutableStateFlow<GeminiAIGenerate>(GeminiAIGenerate.Generating)
        val input = getGeminiContent(modelInputList)

        runCatching {
            generativeModel.generateContentStream(input)
        }.fold(onSuccess = { generateContentResponseFlow ->
            managerScope.launch {
                generateContentResponseFlow.collectLatest { generateContentResponse ->
                    if(generateContentResponse.candidates.isNotEmpty()) {
                        if(generateContentResponse.text.isNullOrEmpty()) {
                            contentFlow.value =
                                GeminiAIGenerate.StreamContentError(defaultErrorMessage)
                        } else {
                            contentFlow.value = GeminiAIGenerate.StreamContentSuccess(
                                generateContentResponse.text!!)
                        }
                    } else {
                        contentFlow.value = GeminiAIGenerate.StreamContentError(defaultErrorMessage)
                    }
                }
            }

        }, onFailure = { throwable ->
            contentFlow.value = GeminiAIGenerate.StreamContentError(
                throwable.message
                    ?: defaultErrorMessage)
        })

        return contentFlow
    }

    private fun getGeminiContent(modelInputList : List<ModelInput>) : Content {
        val geminiModelListInfo = modelInputList.toGeminiModelListInfo()
        return content {
            geminiModelListInfo.textInputList.forEach { content ->
                text(content.text)
            }

            geminiModelListInfo.blobInputList.forEach { blob ->
                blob(blob.mimeType, blob.data)
            }

            geminiModelListInfo.fileInputList.forEach { file ->
                fileData(file.uri, file.mimeType)
            }

            geminiModelListInfo.imageInputList.forEach { image ->
                image(image.bitmap)
            }
        }
    }

    override suspend fun uploadFiles(
            uploadFileInfoList : List<UploadFileInfo>,
            onFileUploadListener : OnFileUploadListener?) : Flow<GeminiAIFileState> {
        return geminiAIFileManager.uploadFiles(uploadFileInfoList, onFileUploadListener)
    }

    override suspend fun getFiles(
            pageSize : Int, pageToken : String) : RemoteResponse<GeminiFileListResponse> {
        return geminiAIFileManager.getFiles(pageSize, pageToken)
    }

    override suspend fun getFileByName(fileName : String) : RemoteResponse<GeminiFileResponse> {
        return geminiAIFileManager.getFileByName(fileName)
    }

    override suspend fun deleteFileByName(fileName : String) : RemoteResponse<Boolean> {
        return geminiAIFileManager.deleteFileByName(fileName)
    }


    override suspend fun generateConversation(
            modelInputHistory: List<ModelInput>, modelInput: ModelInput,
            defaultErrorMessage:String): Flow<String?> {
        val model = generativeModel
        val historyList = mutableListOf<Content>()

        modelInputHistory.forEach {
            when (it) {
                is ModelInput.Image -> {
                    val role = if (it.isUser){
                        "user"
                    }else{
                        "model"
                    }
                    historyList.add(content(role) {
                        image(it.bitmap)
                    })

                }

                is ModelInput.Text -> {
                    val role = if (it.isUser){
                        "user"
                    }else{
                        "model"
                    }
                    historyList.add(content(role) {
                        text(it.text)
                    })
                }

                is ModelInput.Blob -> {

                }
                is ModelInput.File -> {

                }
            }
        }

        val prompt = when (modelInput) {
            is ModelInput.Image -> {
                val role = if (modelInput.isUser){
                    "user"
                }else{
                    "model"
                }
                content(role) {
                    image(modelInput.bitmap)
                }
            }

            is ModelInput.Text -> {
                val role = if (modelInput.isUser){
                    "user"
                }else{
                    "model"
                }
                content(role) {
                    text(modelInput.text)
                }
            }

            is ModelInput.Blob -> {
                val role = if (modelInput.isUser){
                    "user"
                }else{
                    "model"
                }
                content(role) {
                    blob(modelInput.mimeType, modelInput.data)
                }
            }
            is ModelInput.File -> {
                val role = if (modelInput.isUser){
                    "user"
                }else{
                    "model"
                }
                content(role) {
                    fileData(modelInput.uri, modelInput.mimeType)
                }
            }
        }
        val chat = model.startChat(historyList)
        return runCatching {
            chat.sendMessage(prompt)
        }.fold(onSuccess = {
            flow {
                emit(it)
            }.catch {
                val generateContentResponse = GenerateContentResponse(listOf(), null, null)
                emit(generateContentResponse)
            }.map { generateContentResponse: GenerateContentResponse ->
                val text = generateContentResponse.text
                if (text.isNullOrEmpty()) {
                    defaultErrorMessage
                } else {
                    text
                }            }
        }, onFailure = {
            flow {
                emit(it.message)
            }
        })

    }

}