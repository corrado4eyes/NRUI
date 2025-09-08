package com.cradapyx.usecases.impl

import com.cradapyx.nrui.network.NRUIApiClient
import com.cradapyx.nrui.network.model.OssrhStagingApiError
import com.cradapyx.nrui.network.model.UnauthorizedException
import com.cradapyx.nrui.usecases.DeleteRepositoryUseCase

class DeleteRepositoryUseCaseImpl(
    private val apiClient: NRUIApiClient
) : DeleteRepositoryUseCase {
    override suspend fun execute(repositoryKey: String) {
        try {
            apiClient.deleteRepository(repositoryKey)
        } catch (e: UnauthorizedException) {

        } catch (e: OssrhStagingApiError) {

        }
    }
}
