// App.kt — viden-ring entry point
package com.videnring

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import com.videnring.ui.screens.RingScreen

private val VidenColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFD4A843),
    background = androidx.compose.ui.graphics.Color(0xFF0A0A0F),
    surface = androidx.compose.ui.graphics.Color(0xFF12123A)
)

@Composable
fun App() {
    MaterialTheme(colorScheme = VidenColors) {
        RingScreen()
    }
}
