package eu.peernetwork.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val DarkScheme = darkColorScheme(
    primary = PrimaryColor,
    tertiary = LightGray,
    secondary = AccentColor,
    background = Gray5,
    surface = Gray5,
    surfaceVariant = Gray10,
    surfaceDim = Gray60,
    surfaceTint = Gray50,
    onPrimary = White,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,
    onTertiary = Gray5,
    tertiaryContainer = Gray,
    error = PeerAppDarkRed,
    errorContainer = PeerAppRed,
    onError = White
)

internal val LightScheme = lightColorScheme(
    primary = PrimaryColor,
    tertiary = Gray5,
    secondary = AccentColor,
    background = White,
    surface = White,
    surfaceVariant = White70,
    surfaceDim = Gray60,
    surfaceTint = Gray50,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
    onTertiary = LightGray,
    tertiaryContainer = White80,
    error = PeerAppDarkRed,
    errorContainer = PeerAppRed,
    onError = White
)
