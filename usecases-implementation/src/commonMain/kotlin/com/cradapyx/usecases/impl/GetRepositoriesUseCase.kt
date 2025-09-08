package com.cradapyx.usecases.impl

import com.cradapyx.nrui.network.NRUIApiClient
import com.cradapyx.nrui.network.model.OssrhStagingApiError
import com.cradapyx.nrui.network.model.UnauthorizedException
import com.cradapyx.nrui.usecases.GetRepositoriesUseCase

class GetRepositoriesUseCase(
    private val apiClient: NRUIApiClient
) : GetRepositoriesUseCase {
    override suspend fun execute(profileId: String) {
        try {
            apiClient.getRepositories(profileId = profileId)
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: OssrhStagingApiError) {
            throw e // TODO: expose to view as an alert
        }
    }
}