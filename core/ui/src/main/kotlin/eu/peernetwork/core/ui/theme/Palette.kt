package eu.peernetwork.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Primary100 = Color(0xFFe1e6ff)
val Primary200 = Color(0xFFadbcff)
val Primary300 = Color(0xFF7392ff)
val Primary400 = Color(0xFF0069FF)
val Primary500 = Color(0xFF0049b7)
val Primary600 = Color(0xFF002b74)
val Primary700 = Color(0xFF001036)

val Secondary100 = Color(0xFFedf0ff)
val Secondary200 = Color(0xFFbec9ff)
val Secondary300 = Color(0xFF86a0ff)
val Secondary400 = Color(0xFF3b77ff)
val Secondary500 = Color(0xFF0054CC)
val Secondary600 = Color(0xFF003588)
val Secondary700 = Color(0xFF001949)

val Tertiary100 = Color(0xFFc9d6ff)
val Tertiary200 = Color(0xFF8caeff)
val Tertiary300 = Color(0xFF3387FF)
val Tertiary400 = Color(0xFF0063c9)
val Tertiary500 = Color(0xFF00428a)
val Tertiary600 = Color(0xFF002450)
val Tertiary700 = Color(0xFF00102b)

val White50 = Color(0xFFFFFFFF)
val White100 = Color(0xFFFAFAFB)
val White200 = Color(0xFFF5F5F7)
val White300 = Color(0xFFF0F0F2)
val White400 = Color(0xFFEBEBED)

val Gray100 = Color(0xFFD2D2D5)
val Gray125 = Color(0xFFD0D0D3)
val Gray150 = Color(0xFFC8C8CC)
val Gray200 = Color(0xFFbebec2)
val Gray225 = Color(0xFFABABB0)
val Gray250 = Color(0xFFA1A2A7)
val Gray300 = Color(0xFF97989e)
val Gray400 = Color(0xFF72737b)
val Gray500 = Color(0xFF505057)
val Gray550 = Color(0xFF48484F)
val Gray600 = Color(0xFF303034)
val Gray625 = Color(0xFF252629)
val Gray650 = Color(0xFF212225)
val Gray700 = Color(0xFF121315)

internal val LightPalette = lightColorScheme(
    primary = Primary400,
    onPrimary = White50,
    primaryContainer = Primary200,
    onPrimaryContainer = Primary700,
    secondary = Secondary300,
    onSecondary = White50,
    secondaryContainer = Secondary100,
    onSecondaryContainer = Secondary700,
    tertiary = Tertiary300,
    onTertiary = White50,
    tertiaryContainer = Tertiary100,
    onTertiaryContainer = Tertiary700,
    background = White50,
    onBackground = Gray700,
    surface = White50,
    onSurface = Gray700,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,
    outline = Gray300,
    outlineVariant = Gray225,
    inverseSurface = Gray700,
    inverseOnSurface = Gray100,
    inversePrimary = Primary100,
    surfaceTint = Primary300,
    surfaceContainerLow = Gray150,
    surfaceContainerLowest = Gray125,
    surfaceContainerHigh = Primary600,
    surfaceContainerHighest = Gray650,
    scrim = Gray250
)

internal val DarkPalette = darkColorScheme(
    primary = Primary400,
    onPrimary = Primary100,
    primaryContainer = Primary600,
    onPrimaryContainer = Primary100,
    secondary = Secondary200,
    onSecondary = Secondary700,
    secondaryContainer = Secondary600,
    onSecondaryContainer = Secondary100,
    tertiary = Tertiary200,
    onTertiary = Tertiary700,
    tertiaryContainer = Tertiary600,
    onTertiaryContainer = Tertiary100,
    background = Gray700,
    onBackground = Gray100,
    surface = Gray700,
    onSurface = Gray100,
    surfaceVariant = Gray600,
    onSurfaceVariant = Gray200,
    outline = Gray400,
    outlineVariant = Gray500,
    inverseSurface = Gray100,
    inverseOnSurface = Gray700,
    inversePrimary = Primary600,
    surfaceTint = Primary200,
    surfaceContainerLow = Gray625,
    surfaceContainerLowest = Gray650,
    surfaceContainerHigh = Primary100,
    surfaceContainerHighest = White50,
    scrim = Gray550
)
