package com.androidai.framework.shared.core.file.data.response.error


data class GeminiFileRemoteError(
        val statusCode: Int,
        val url: String,
        val errorCode: String,
        val response: String,
        val message: String) {

    companion object {
        fun getEmptyError(statusCode:Int, url: String, errorCode: String,
                          response: String): GeminiFileRemoteError {
            return GeminiFileRemoteError(
                statusCode = statusCode, message = "", url = url, response = response,
                errorCode = errorCode)
        }
    }
}