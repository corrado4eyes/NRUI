package com.cradapyx.usecases.impl

import com.cradapyx.nrui.common.PublishingType
import com.cradapyx.nrui.network.NRUIApiClient
import com.cradapyx.nrui.network.model.OssrhStagingApiError
import com.cradapyx.nrui.network.model.UnauthorizedException
import com.cradapyx.nrui.usecases.UploadRepositoryUseCase

class UploadRepositoryUseCase(
    private val apiClient: NRUIApiClient
) : UploadRepositoryUseCase {
    override suspend fun execute(
        repositoryKey: String,
        deploymentName: String,
        publishingType: PublishingType
    ) {
        try {
            apiClient.uploadRepository(
                repositoryKey = repositoryKey,
                deploymentName = deploymentName,
                publishingType = publishingType
            )
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: OssrhStagingApiError) {
            throw e // TODO: expose to view as an alert
        }
    }
}