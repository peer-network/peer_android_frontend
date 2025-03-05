package eu.peernetwork.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val DarkScheme = darkColorScheme(
    primary = PeerAppBlue,
    onPrimary = PeerAppWhite,
    secondary = PeerAppDarkBlue,
    onSecondary = PeerAppWhite,
    surface = PeerAppBlack,
    onSurface = PeerAppWhite,
    background = PeerAppDarkGray,
    onBackground = PeerAppWhite,
    error = PeerAppRed,
    onError = PeerAppBlack
)

internal val LightScheme = lightColorScheme(
    primary = PeerAppBlue,
    onPrimary = PeerAppWhite,
    secondary = PeerAppDarkBlue,
    onSecondary = PeerAppWhite,
    surface = PeerAppLightGray,
    onSurface = PeerAppBlack,
    background = PeerAppWhite,
    onBackground = PeerAppBlack,
    error = PeerAppRed,
    onError = PeerAppWhite
)
