package com.androidai.framework.shared.core.file.utils

import io.ktor.client.request.HttpRequestBuilder


suspend fun HttpRequestBuilder.appendConfiguration(apiKey : String){
    headers.append(
        "x-goog-api-key",
        apiKey
    )
}