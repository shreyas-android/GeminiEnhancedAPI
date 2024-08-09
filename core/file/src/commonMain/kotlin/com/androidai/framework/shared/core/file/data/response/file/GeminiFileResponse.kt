package com.androidai.framework.shared.core.file.data.response.file

import kotlinx.serialization.Serializable

@Serializable
data class GeminiFileResponse(
        val name : String, val displayName:String? = null, val mimeType : String, val sizeBytes : String, val createTime : String,
        val updateTime : String, val expirationTime : String, val sha256Hash : String,
        val uri : String, val state : String,
        val videoMetaDataResponse : VideoMetaDataResponse? = null)


@Serializable
data class VideoMetaDataResponse(
        val videoDuration:String)