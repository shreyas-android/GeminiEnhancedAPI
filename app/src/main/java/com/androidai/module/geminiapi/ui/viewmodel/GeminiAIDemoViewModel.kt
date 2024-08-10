package com.androidai.module.geminiapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidai.framework.shared.core.model.manager.GeminiAIManager
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.cogniheroid.framework.feature.appaai.ui.generation.advancetextgeneration.uistate.GeminiAIDemoUIEffect
import com.androidai.module.geminiapi.uistate.GeminiAIDemoUIEvent
import com.androidai.module.geminiapi.uistate.GeminiAIDemoUIState
import com.androidai.module.geminiapi.utils.FileUriInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GeminiAIDemoViewModel(
        private val avengerAIManager : GeminiAIManager) : ViewModel() {

    private val inputField = MutableStateFlow("")

    private var result = MutableStateFlow("")

    private var isModelStartedGeneratingText = MutableStateFlow(false)

    private var fileUriInfoItemsFlow : MutableStateFlow<List<FileUriInfo>> =
        MutableStateFlow(listOf())

    val geminiAIDemoUIStateStateFlow = combine(
        inputField, result, isModelStartedGeneratingText,
        fileUriInfoItemsFlow) { inputText, outputText, isGenerating, bitmapList ->
        GeminiAIDemoUIState(inputText, outputText, isGenerating, bitmapList)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        GeminiAIDemoUIState("", "", false, listOf()))

    private val _geminiAIDemoUIEffectChannel = Channel<GeminiAIDemoUIEffect>()
    val fatherAIInfoUIEffectFlow = _geminiAIDemoUIEffectChannel.receiveAsFlow()

    fun performIntent(textGenerationUIEvent : GeminiAIDemoUIEvent) {
        when(textGenerationUIEvent) {
            is GeminiAIDemoUIEvent.GenerateText -> {
                isModelStartedGeneratingText.value = true
                clearResult()
                generateTextAndUpdateResult(
                    fileUriInfoItemsFlow.value, textGenerationUIEvent.text,
                    textGenerationUIEvent.defaultErrorMessage)
            }

            is GeminiAIDemoUIEvent.InputText -> {
                inputField.value = textGenerationUIEvent.text
            }

            GeminiAIDemoUIEvent.ClearText -> {
                inputField.value = ""
            }

            is GeminiAIDemoUIEvent.RemoveFileItem -> {
                val mutableList = fileUriInfoItemsFlow.value.toMutableList()
                mutableList.remove(textGenerationUIEvent.fileUriInfo)
                fileUriInfoItemsFlow.value = mutableList

            }

            is GeminiAIDemoUIEvent.OnOpenFilePicker -> {
                setSideEffect(GeminiAIDemoUIEffect.LaunchPicker)
            }

            is GeminiAIDemoUIEvent.UpdateFileUriItems -> {
                fileUriInfoItemsFlow.value = textGenerationUIEvent.fileUriInfoItems
            }

            GeminiAIDemoUIEvent.DeleteFirstFile -> {
                deleteFilesByName()
            }

            GeminiAIDemoUIEvent.GetAllFiles -> {
                getFiles()
            }

            GeminiAIDemoUIEvent.GetFile -> {
getFileByName()
            }
        }
    }

    private fun generateTextAndUpdateResult(
            fileUriItems : List<FileUriInfo>, prompt : String, defaultErrorMessage : String) {
        viewModelScope.launch {

            avengerAIManager.generateTextStreamContent(
                getModelInputList(fileUriItems, prompt), null, defaultErrorMessage,
            ).collectLatest {

                result.value = it.toString()
                isModelStartedGeneratingText.value = false
            }
        }
    }

    fun getFiles() {
        result.value = "Getting all files..."
        viewModelScope.launch {
            when(val response = avengerAIManager.getFiles()) {
                is RemoteResponse.Error -> {
                    result.value = response.toString()
                }

                is RemoteResponse.Success -> {
                    result.value = response.toString()
                }
            }
        }
    }

    private fun getFileByName() {
        result.value = "Getting files by name  ..."
        viewModelScope.launch {
            when(val filesResponse = avengerAIManager.getFiles()) {
                is RemoteResponse.Error -> {
                    result.value = filesResponse.toString()
                }

                is RemoteResponse.Success -> {
                    val fileName = filesResponse.data.fileItems[0].name
                    when(val response = avengerAIManager.getFileByName(fileName)) {
                        is RemoteResponse.Error -> {
                            result.value = response.toString()
                        }

                        is RemoteResponse.Success -> {
                            result.value = response.toString()
                        }
                    }
                }
            }

        }
    }

    private fun deleteFilesByName() {
        result.value = "Deleting files by name  ..."
        viewModelScope.launch {
            when(val filesResponse = avengerAIManager.getFiles()) {
                is RemoteResponse.Error -> {
                    result.value = filesResponse.toString()
                }

                is RemoteResponse.Success -> {
                    val fileName = filesResponse.data.fileItems[0].name
                    when(val response = avengerAIManager.deleteFileByName(fileName)) {
                        is RemoteResponse.Error -> {
                            result.value = response.toString()
                        }

                        is RemoteResponse.Success -> {
                            result.value = response.toString()
                        }
                    }
                }
            }
        }
    }

    private fun getModelInputList(
            fileUriItems : List<FileUriInfo>, text : String) : List<ModelInput> {
        val modelInputList = mutableListOf<ModelInput>()
        fileUriItems.forEach {
            modelInputList.add(
                ModelInput.File(
                    true, fileName = it.name, mimeType = it.mimeType, data = it.data
                        ?: ByteArray(0), uri = it.path))
        }
        modelInputList.add(ModelInput.Text(true, text))
        return modelInputList
    }

    private fun clearResult() {
        result.value = ""
    }

    private fun setSideEffect(geminiAIDemoUIEffect : GeminiAIDemoUIEffect) {
        viewModelScope.launch {
            _geminiAIDemoUIEffectChannel.send(geminiAIDemoUIEffect)
        }
    }
}