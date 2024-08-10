package com.androidai.framework.shared.core.file.client.exception

import com.androidai.framework.shared.core.file.data.response.error.GeminiFileRemoteError

data class GeminiFileResponseException(val geminiFileRemoteError: GeminiFileRemoteError) : Exception(geminiFileRemoteError.message.toString())


