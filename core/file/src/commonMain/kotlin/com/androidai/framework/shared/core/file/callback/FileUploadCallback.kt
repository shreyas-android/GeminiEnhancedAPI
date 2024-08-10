package com.androidai.framework.shared.core.file.callback

interface FileUploadCallback{

    fun onUpload( bytesSentTotal:Long, contentLength:Long)
}