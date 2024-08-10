package com.androidai.framework.shared.core.file.data.response.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiFileUploadResponse(
        @SerialName("file")
        val file : GeminiFileResponse)