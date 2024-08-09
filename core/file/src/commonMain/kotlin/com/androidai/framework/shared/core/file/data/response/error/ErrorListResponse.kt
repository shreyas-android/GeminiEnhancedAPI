package com.androidai.framework.shared.core.file.data.response.error


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorListResponse(
        @SerialName("error")
        val errorResponse: List<ErrorResponse>)

@Serializable
data class ErrorResponse(
        @SerialName("description")
        val description: String,
        @SerialName("message")
        val message: String,
)