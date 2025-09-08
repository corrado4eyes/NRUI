package com.cradapyx.nrui.usecases

import com.cradapyx.nrui.common.PublishingType

interface GetRepositoriesUseCase {
    suspend fun execute(profileId: String)
}