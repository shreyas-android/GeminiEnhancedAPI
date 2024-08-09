package com.androidai.framework.shared.core.file.remote.path


internal class RemoteUrlPath() {

    private fun getAPIBaseURL() = "https://generativelanguage.googleapis.com"

    fun uploadFileUrl() = "${getAPIBaseURL()}/upload/v1beta/files"

    fun getFileByNameUrl(fileName:String) = "${getAPIBaseURL()}/v1beta/$fileName"

    fun deleteFileByNameUrl(fileName:String) = "${getAPIBaseURL()}/v1beta/$fileName"

    fun getFilesUrl() = "${getAPIBaseURL()}/v1beta/files"

}