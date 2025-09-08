package com.cradapyx.nrui.network

interface NRUIApiClient {
    suspend fun uploadArtifact()
    suspend fun deleteArtifact()
    suspend fun getArtifacts()
}