package com.androidai.module.geminiapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidai.framework.shared.core.model.manager.GeminiAIManager

class GeminiAIDemoViewModelFactory(private val avengerAIManager: GeminiAIManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GeminiAIDemoViewModel(avengerAIManager) as T
    }
}