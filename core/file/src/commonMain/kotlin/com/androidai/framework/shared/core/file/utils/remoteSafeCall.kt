package com.androidai.framework.shared.core.file.utils

import com.androidai.framework.shared.core.file.client.exception.GeminiFileResponseException
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.file.data.response.error.GeminiFileRemoteError
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

inline fun <T> remoteSafeCall(action: () -> HttpResponse, onSuccess: (value: HttpResponse) -> T,
                              onLogFailure:(GeminiFileRemoteError)->Unit,
                              defaultErrorValue:String= ""): RemoteResponse<T> {
    return runCatching {
        action()
    }.fold(onSuccess = {
        RemoteResponse.Success(onSuccess(it))
    }, onFailure = {
        when (it) {
            is GeminiFileResponseException -> {
                onLogFailure(it.geminiFileRemoteError)
                RemoteResponse.Error(
                    it.geminiFileRemoteError
                )
            }
            else ->{
                val calendarRemoteError =
                    GeminiFileRemoteError(
                        statusCode = 0, url = "", errorCode = "", response = "",
                        message = it.message
                            ?: defaultErrorValue)
                onLogFailure(calendarRemoteError)
                RemoteResponse.Error(calendarRemoteError)
            }
        }
    })
}

suspend fun <T> Flow<T>.safeCollectFlowFirst(): T? {
    return runCatching {
        first()
    }.fold(onSuccess = {
        it
    }, onFailure = {
        null
    })
}