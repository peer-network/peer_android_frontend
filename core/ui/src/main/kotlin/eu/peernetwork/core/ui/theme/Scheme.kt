package eu.peernetwork.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val DarkScheme = darkColorScheme(
    primary = PeerAppBlue,
    onPrimary = PeerAppWhite,
    secondary = PeerAppDarkBlue,
    onSecondary = PeerAppWhite,
    surface = PeerAppGray25,
    onSurface = PeerAppGray90,
    surfaceBright = PeerAppGray95,
    surfaceDim = PeerAppGray85,
    surfaceTint = PeerAppGray15,
    surfaceVariant = PeerAppWhite,
    onSurfaceVariant = PeerAppGray60,
    background = PeerAppBlack,
    onBackground = PeerAppWhite,
    error = PeerAppRed,
    onError = PeerAppBlack,
)

internal val LightScheme = lightColorScheme(
    primary = PeerAppBlue,
    onPrimary = PeerAppWhite,
    secondary = PeerAppDarkBlue,
    onSecondary = PeerAppWhite,
    surface = PeerAppGray95,
    onSurface = PeerAppGray50,
    surfaceBright = PeerAppGray95,
    surfaceDim = PeerAppGray85,
    surfaceTint = PeerAppGray15,
    surfaceVariant = PeerAppBlack,
    onSurfaceVariant = PeerAppGray60,
    background = PeerAppWhite,
    onBackground = PeerAppBlack,
    error = PeerAppRed,
    onError = PeerAppWhite
)
