package com.androidai.framework.shared.core.file.utils

import io.ktor.client.plugins.logging.Logger

class FrameworkLogger(private val isDebug: Boolean) : Logger {
    private val tag = "FrameworkLogger"
    override fun log(message: String) {
        LogUtil.v(tag, "message $message")
    }
}