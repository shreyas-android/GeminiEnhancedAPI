package com.androidai.framework.shared.core.file.client.exception

import com.androidai.framework.shared.core.file.data.mapper.toGeminiFileRemoteError
import com.androidai.framework.shared.core.file.data.response.error.ErrorListResponse
import com.androidai.framework.shared.core.file.data.response.error.GeminiFileRemoteError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request


// TODO: Should handle empty message for error
object ApiExceptionHandler {

    private suspend fun throwError(exceptionResponse: HttpResponse) {
        val statusCode = exceptionResponse.status.value
        val url = exceptionResponse.request.url.toString()
        val response = exceptionResponse.bodyAsText()

        runCatching { exceptionResponse.body<ErrorListResponse>() }.fold(onSuccess = {
            val errorCode = ""
            throw GeminiFileResponseException(
                it.errorResponse.getOrNull(0)?.toGeminiFileRemoteError(
                    statusCode = statusCode, url = url, errorCode = errorCode, response = response)
                    ?: GeminiFileRemoteError.getEmptyError(
                            statusCode = statusCode, url = url, errorCode = errorCode,
                            response = response))
        }, onFailure = {
                throw GeminiFileResponseException(
                    GeminiFileRemoteError.getEmptyError(
                        statusCode = statusCode, url = url, errorCode = "", response = response))
        })
    }

    suspend fun handleServerException(exceptionResponse: HttpResponse) {
        throwError(
            exceptionResponse)
    }

    suspend fun handleRedirectResponseException(exceptionResponse: HttpResponse) {
        throwError(
            exceptionResponse)
    }

    suspend fun handleResponseException(exceptionResponse: HttpResponse) {
        throwError(
            exceptionResponse)
    }

    suspend fun handleClientRequestException(exceptionResponse: HttpResponse) {
        throwError(
            exceptionResponse)
    }

    fun handleRequestTimeoutException(exception: Throwable) {
        throw GeminiFileResponseException(
            GeminiFileRemoteError.getEmptyError(
                statusCode = 101, url = "", errorCode = "", response = ""))
    }

    fun handleConnectionTimeoutException(exception: Throwable) {
        throw GeminiFileResponseException(
            GeminiFileRemoteError.getEmptyError(
                statusCode = 101, url = "", errorCode = "", response = ""))
    }

    fun handleSocketTimeoutException(exception: Throwable) {
        throw GeminiFileResponseException(
            GeminiFileRemoteError.getEmptyError(
                statusCode = 101, url = "", errorCode = "", response = ""))
    }


}
