package com.cradapyx.nrui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun NRUITheme(content: @Composable () -> Unit) {
    MaterialTheme {
        CompositionLocalProvider(
            // Inject themes classes
        ) {
            content()
        }
    }
}