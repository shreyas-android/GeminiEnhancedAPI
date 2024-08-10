package com.androidai.framework.shared.core.model.flow

import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
        initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue))