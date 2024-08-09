package com.androidai.framework.shared.core.file.data.request

data class GeminiFileUploadRequest(
    val fileName: String,
    val contentType : String,
    val fileData: ByteArray,
)