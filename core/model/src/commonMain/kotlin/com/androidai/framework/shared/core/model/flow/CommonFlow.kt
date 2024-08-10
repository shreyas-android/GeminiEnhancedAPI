package com.androidai.framework.shared.core.model.flow

import kotlinx.coroutines.flow.Flow

expect class CommonFlow<T>(flow: Flow<T>) : Flow<T>

fun <T> Flow<T>.toCommonFlow() =
    com.androidai.framework.shared.core.model.flow.CommonFlow(this)