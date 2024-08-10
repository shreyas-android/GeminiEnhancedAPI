package com.androidai.framework.shared.core.file.data.mapper

import com.androidai.framework.shared.core.file.data.response.error.GeminiFileRemoteError
import com.androidai.framework.shared.core.file.data.response.error.ErrorResponse

internal fun ErrorResponse.toGeminiFileRemoteError(statusCode: Int, url:String, errorCode:String, response:String) =
        GeminiFileRemoteError(
                message = this.message, url = url, errorCode = errorCode, response = response,
                statusCode = statusCode)