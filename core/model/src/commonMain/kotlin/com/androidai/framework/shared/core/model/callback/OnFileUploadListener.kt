package com.androidai.framework.shared.core.model.callback

import com.androidai.framework.shared.core.model.data.model.ModelInput

interface OnFileUploadListener {

    fun onUploadFile(bytesSentTotal:Long, contentLength:Long, file: ModelInput.File)
}