package com.androidai.framework.shared.core.file

import com.cogniheroid.framework.shared.core.geminifile.remote.path.RemoteUrlPath
import com.cogniheroid.framework.shared.core.geminifile.remote.remoteservice.FileRemoteService
import com.cogniheroid.framework.shared.core.geminifile.repository.FileRepositoryImpl

object Instance {

   private val networkClient by lazy {
        com.androidai.framework.shared.core.file.client
            .NetworkClient(com.androidai.framework.shared.core.file.GeminiFileCore.isDebug).getNetworkClient()
    }

    private val remoteUrlPath by lazy {
        RemoteUrlPath()
    }

    private val fileRemoteService by lazy {
        FileRemoteService(
            com.androidai.framework.shared.core.file.Instance.remoteUrlPath,
            com.androidai.framework.shared.core.file.GeminiFileCore.apiKey,
            com.androidai.framework.shared.core.file.Instance.networkClient)
    }

    internal val  fileRepositoryImpl by lazy {
        FileRepositoryImpl(
            com.androidai.framework.shared.core.file.Instance.fileRemoteService)
    }


}