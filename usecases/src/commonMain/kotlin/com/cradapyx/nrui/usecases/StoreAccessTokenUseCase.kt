package com.cradapyx.nrui.usecases

// TODO: Will implement using finger print or user password
interface StoreAccessTokenUseCase {
    fun execute(userToken: String, userTokenPassword: String)
}
