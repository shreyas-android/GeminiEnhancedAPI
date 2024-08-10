package com.androidai.framework.shared.core.file.data.response.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiFileListResponse(
        @SerialName("files")
        val fileItems :List<GeminiFileResponse> = listOf(),

        @SerialName("nextPageToken")
        val nextPageToken:String? = null
)