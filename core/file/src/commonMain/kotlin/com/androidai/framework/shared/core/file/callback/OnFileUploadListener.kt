package com.androidai.framework.shared.core.file.callback

interface OnFileUploadListener{

    fun onUpload( bytesSentTotal:Long, contentLength:Long)
}