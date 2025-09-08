package com.cradapyx.nrui.usecases

interface DeleteRepositoryUseCase {
    suspend fun execute(repositoryKey: String)
}
