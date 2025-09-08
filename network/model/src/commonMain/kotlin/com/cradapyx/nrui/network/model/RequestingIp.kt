package com.cradapyx.nrui.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RequestingIp {
    @SerialName("any") ANY,
    @SerialName("client") CLIENT,
}