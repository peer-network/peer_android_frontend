package eu.peernetwork.core.ui.extension

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme

fun ColorScheme.isLightTheme(): Boolean {
    return this == lightColorScheme()
}
