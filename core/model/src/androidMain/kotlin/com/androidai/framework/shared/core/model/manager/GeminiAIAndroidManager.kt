package com.androidai.framework.shared.core.model.manager

import android.util.Log
import com.androidai.framework.shared.core.model.callback.OnFileUploadListener
import com.androidai.framework.shared.core.model.flow.toCommonFlow
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.model.flow.CommonFlow
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileListResponse
import com.androidai.framework.shared.core.file.data.response.file.GeminiFileResponse
import com.androidai.framework.shared.core.model.data.enums.GeminiAIGenerate
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class GeminiAIAndroidManager(
        private val apiKey : String, modelName:String,  private val geminiAIFileManager : GeminiAIFileManager) : GeminiAIManager {

    private val managerScope = CoroutineScope(Dispatchers.Default)

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey,
    )

    override suspend fun generateTextStreamContent(
            modelInputList : List<ModelInput>, onFileUploadListener : OnFileUploadListener?,
            defaultErrorMessage : String) : CommonFlow<GeminiAIGenerate> {

        val contentFlow : MutableStateFlow<GeminiAIGenerate> =
            MutableStateFlow(GeminiAIGenerate.Generating)

        val fileInputList = arrayListOf<ModelInput.File>()
        val textInputList = arrayListOf<ModelInput.Text>()
        val blobInputList = arrayListOf<ModelInput.Blob>()

        modelInputList.forEach {
            when(it) {
                is ModelInput.Blob -> {
                    blobInputList.add(it)
                }

                is ModelInput.File -> {
                    fileInputList.add(it)
                }

                is ModelInput.Text -> {
                    textInputList.add(it)
                }
            }
        }

        val filesUploadFlow = geminiAIFileManager.uploadFiles(
            fileInputList as List<ModelInput.File>, onFileUploadListener)

        managerScope.launch {
            filesUploadFlow.collectLatest {
                contentFlow.value = it
                if(it is GeminiAIGenerate.AllFileUploaded) {

                    val input = content {
                        textInputList.forEach { content ->
                            text(content.text)
                        }

                        blobInputList.forEach { blob ->
                            blob(blob.mimeType, blob.data)
                        }

                        it.files.forEach { response ->
                            fileData(response.uri, response.mimeType)
                        }
                    }

                    contentFlow.value = GeminiAIGenerate.Generating

                    runCatching {
                        generativeModel.generateContent(input)
                    }.fold(onSuccess = { generateContentResponse ->
                        if(generateContentResponse.candidates.isNotEmpty()) {
                            if(generateContentResponse.text.isNullOrEmpty()) {
                                contentFlow.value =
                                    GeminiAIGenerate.StreamContentError(defaultErrorMessage)
                            } else {
                                contentFlow.value = GeminiAIGenerate.StreamContentSuccess(
                                    generateContentResponse.text!!)
                            }
                        } else {
                            contentFlow.value =
                                GeminiAIGenerate.StreamContentError(defaultErrorMessage)
                        }

                    }, onFailure = { throwable ->
                        contentFlow.value = GeminiAIGenerate.StreamContentError(
                            throwable.message
                                ?: defaultErrorMessage)
                    })
                }

            }
        }

        return contentFlow.toCommonFlow()
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

}