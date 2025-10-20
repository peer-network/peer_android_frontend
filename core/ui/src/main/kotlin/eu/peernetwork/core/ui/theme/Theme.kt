package eu.peernetwork.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun DesignTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    PeerTheme(
        isDarkMode = isDarkMode,
        colorScheme = when {
            isDarkMode -> DarkPalette
            else -> LightPalette
        },
        content = content
    )
}

@Composable
fun PeerTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = when {
        isDarkMode -> DarkScheme
        else -> LightScheme
    },
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    SideEffect {
        view.context as? Activity ?: return@SideEffect
        val window = (view.context as Activity).window
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        val windowsInsetsController = WindowCompat.getInsetsController(window, view)
        windowsInsetsController.isAppearanceLightStatusBars = !isDarkMode
        windowsInsetsController.isAppearanceLightNavigationBars = !isDarkMode
    }
    PeerTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun PeerTheme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
