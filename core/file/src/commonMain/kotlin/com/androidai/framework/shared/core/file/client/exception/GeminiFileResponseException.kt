package com.androidai.framework.shared.core.file.client.exception

import com.cogniheroid.framework.shared.core.geminifile.data.response.error.GeminiFileRemoteError

data class GeminiFileResponseException(val calendarError: GeminiFileRemoteError) : Exception(calendarError.message.toString())


