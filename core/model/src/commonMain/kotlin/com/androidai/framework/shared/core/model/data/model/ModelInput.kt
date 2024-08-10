package com.androidai.framework.shared.core.model.data.model


sealed class ModelInput {

    data class File(val isUser:Boolean, val fileName:String,  val mimeType: String,
                    val data:ByteArray,
                    val uri:String): ModelInput()

    data class Blob(val isUser:Boolean, val mimeType: String,
                    val data:ByteArray): ModelInput()

    data class Text(val isUser:Boolean, val text: String): ModelInput()
}