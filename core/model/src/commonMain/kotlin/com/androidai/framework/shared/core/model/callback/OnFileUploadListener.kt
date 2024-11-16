package com.androidai.framework.shared.core.model.callback

import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.model.data.model.UploadFileInfo

interface OnFileUploadListener {



    fun onUploadFile(bytesSentTotal:Long, contentLength:Long, uploadFile: UploadFileInfo)
}