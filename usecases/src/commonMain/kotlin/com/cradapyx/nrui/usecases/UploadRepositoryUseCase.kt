package com.cradapyx.nrui.usecases

import com.cradapyx.nrui.common.PublishingType

interface UploadRepositoryUseCase {
    suspend fun execute(
        repositoryKey: String,
        deploymentName: String,
        publishingType: PublishingType
    )
}
