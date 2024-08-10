package com.androidai.framework.shared.core.file.client


import com.androidai.framework.shared.core.file.utils.FrameworkLogger
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class NetworkClient(isDebug : Boolean) {

    fun getNetworkClient(): HttpClient = httpClient

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = FrameworkLogger(isDebug)
            level = LogLevel.ALL
        }
        install(HttpTimeout){
            requestTimeoutMillis = 1000 * 60
            connectTimeoutMillis = 1000 * 60
            socketTimeoutMillis = 1000 * 60
        }
        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is ClientRequestException -> com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleClientRequestException(exception.response)
                    is ServerResponseException -> com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleServerException(exception.response)
                    is RedirectResponseException -> com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleRedirectResponseException(exception.response)
                    is ResponseException -> com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleResponseException(exception.response)
                    is HttpRequestTimeoutException ->  com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleRequestTimeoutException(exception)
                    is ConnectTimeoutException -> { com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleConnectionTimeoutException(exception) }
                    is SocketTimeoutException -> { com.androidai.framework.shared.core.file.client.exception.ApiExceptionHandler.handleSocketTimeoutException(exception) }
                }
            }
        }
    }

}