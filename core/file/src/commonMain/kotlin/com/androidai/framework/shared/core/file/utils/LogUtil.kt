package com.androidai.framework.shared.core.file.utils

internal object LogUtil {

    fun v(module: String, message: String) {
            println("$module - $message")
    }
}