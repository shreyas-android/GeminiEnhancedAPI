package com.androidai.framework.shared.core.file.data.response

import com.cogniheroid.framework.shared.core.geminifile.data.response.error.GeminiFileRemoteError

sealed class RemoteResponse<out T> {
    data class Success<T>(val data: T) : RemoteResponse<T>()
    data class Error(val calendarRemoteError: GeminiFileRemoteError) : RemoteResponse<Nothing>()
}