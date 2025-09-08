package com.cradapyx.nrui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform