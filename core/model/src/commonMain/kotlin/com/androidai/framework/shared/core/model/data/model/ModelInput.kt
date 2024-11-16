package com.androidai.framework.shared.core.model.data.model

import dev.shreyaspatil.ai.client.generativeai.type.Bitmap
sealed class ModelInput {
    data class File(
            val isUser : Boolean, val fileName : String, val mimeType : String,
            val uri : String) : ModelInput()

    data class Blob(
            val isUser : Boolean, val mimeType : String,
            val data : ByteArray) : ModelInput()

    data class Text(val isUser : Boolean, val text : String) : ModelInput()

    data class Image(val isUser : Boolean, val bitmap : Bitmap) : ModelInput()
}

data class UploadFileInfo(
        val isUser : Boolean, val fileName : String, val mimeType : String,
        val data : ByteArray)