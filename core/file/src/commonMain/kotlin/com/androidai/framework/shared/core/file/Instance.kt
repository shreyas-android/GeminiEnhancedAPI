package com.androidai.framework.shared.core.file

import com.androidai.framework.shared.core.file.remote.path.RemoteUrlPath
import com.androidai.framework.shared.core.file.remote.remoteservice.FileRemoteService
import com.androidai.framework.shared.core.file.repository.FileRepositoryImpl


object Instance {

   private val networkClient by lazy {
        com.androidai.framework.shared.core.file.client
            .NetworkClient(GeminiFileCore.isDebug).getNetworkClient()
    }

    private val remoteUrlPath by lazy {
        RemoteUrlPath()
    }

    private val fileRemoteService by lazy {
        FileRemoteService(
            remoteUrlPath,
            GeminiFileCore.apiKey,
            networkClient)
    }

    internal val  fileRepositoryImpl by lazy {
        FileRepositoryImpl(
            fileRemoteService)
    }


}