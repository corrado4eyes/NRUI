package com.cradapyx.nrui.service

class AccessTokenServiceMock : AccessTokenService {

    var accessTokenReturns: String? = null
    override fun accessToken(): String {
        requireNotNull(accessTokenReturns)

        return accessTokenReturns!!
    }
}
