package com.cradapyx.nrui.network

import com.cradapyx.nrui.network.model.PublishingType
import com.cradapyx.nrui.network.model.RepositoriesRemote
import com.cradapyx.nrui.network.model.RepositoryState
import com.cradapyx.nrui.network.model.RequestingIp

interface NRUIApiClient {
    suspend fun uploadRepository(
        repositoryKey: String,
        deploymentName: String,
        publishingType: PublishingType
    )
    suspend fun deleteRepository(
        repositoryKey: String,
    )
    suspend fun getRepositories(
        profileId: String? = null,
        state: RepositoryState = RepositoryState.OPEN,
        ip: RequestingIp = RequestingIp.ANY,
    ): RepositoriesRemote
}