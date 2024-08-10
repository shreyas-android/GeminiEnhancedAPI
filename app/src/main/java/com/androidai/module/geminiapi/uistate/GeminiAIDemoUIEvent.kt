package com.androidai.module.geminiapi.uistate

import com.androidai.module.geminiapi.utils.FileUriInfo

sealed class GeminiAIDemoUIEvent {

    data class InputText(val text: String) : GeminiAIDemoUIEvent()
    data class GenerateText(val text: String, val defaultErrorMessage:String) : GeminiAIDemoUIEvent()

    object ClearText : GeminiAIDemoUIEvent()

    object OnOpenFilePicker : GeminiAIDemoUIEvent()

    data class RemoveFileItem(val fileUriInfo : FileUriInfo) : GeminiAIDemoUIEvent()

    object GetAllFiles:GeminiAIDemoUIEvent()

    object DeleteFirstFile:GeminiAIDemoUIEvent()

    object GetFile:GeminiAIDemoUIEvent()

    data class UpdateFileUriItems( val fileUriInfoItems:List<FileUriInfo>): GeminiAIDemoUIEvent()
}