package com.cradapyx.nrui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Nexus Repository UI",
    ) {
        App()
    }
}