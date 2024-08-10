package com.androidai.framework.shared.core.model.flow

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

actual open class CommonStateFlow<T> actual constructor(
        private val flow: StateFlow<T>
) : com.androidai.framework.shared.core.model.flow.CommonFlow<T>(flow), StateFlow<T> {
    override val replayCache: List<T>
        get() = flow.replayCache
    override val value: T
        get() = flow.value

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        flow.collect(collector)
    }
}