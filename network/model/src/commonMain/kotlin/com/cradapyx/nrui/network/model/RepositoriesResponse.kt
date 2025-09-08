package com.cradapyx.nrui.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoriesRemote(
    val repositories: List<RepositoryRemote>
)

@Serializable
data class RepositoryRemote(
    @SerialName("key")
    val id: String,
    val description: String,
    @SerialName("portal_deployment_id")
    val portalDeploymentId: String,
    val state: RepositoryState
)

@Serializable
enum class RepositoryState {
    @SerialName("open") OPEN,
    @SerialName("closed") CLOSED,
    @SerialName("released") RELEASED;
}
