package com.androidai.framework.shared.core.file.utils

import io.ktor.client.request.HttpRequestBuilder

object RemoteUtils {

    const val GENERIC_STATUS_CODE = 901

}

suspend fun HttpRequestBuilder.appendConfiguration(apiKey : String){
    headers.append(
        "x-goog-api-key",
        apiKey
    )
}