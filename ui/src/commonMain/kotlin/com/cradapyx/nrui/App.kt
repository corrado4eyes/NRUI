package com.cradapyx.nrui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    NRUITheme {
        var userToken by remember { mutableStateOf("") }
        var userTokenPassword by remember { mutableStateOf("") }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextField(
                value = userToken,
                onValueChange = {
                    userToken = it
                }
            )

            TextField(
                value = userTokenPassword,
                onValueChange = {
                    userTokenPassword = it
                }
            )
        }
    }
}