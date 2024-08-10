package com.androidai.framework.shared.core.model.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

actual open class CommonFlow<T> actual constructor(
        private val flow: Flow<T>
) : Flow<T> by flow {

    fun subscribe(
            dispatcher: CoroutineDispatcher,
            onCollect: (T) -> Unit
    ): DisposableHandle {
        val job = CoroutineScope(dispatcher).launch {
            flow.collect(onCollect)
        }
        return DisposableHandle { job.cancel() }
    }

    fun subscribe(
            onCollect: (T) -> Unit
    ): DisposableHandle {
        return subscribe(
                dispatcher = Dispatchers.Default,
                onCollect = onCollect
        )
    }
}