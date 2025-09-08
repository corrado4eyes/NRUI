package com.cradapyx.nrui.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OssrhStagingApiError(val error: String) : Throwable()

class UnauthorizedException(): Throwable()
