package vm.words.ua.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

object AppTheme {
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)

    val PrimaryGreen = Color(0xFF44F2C1)
    val PrimaryGreen60 = Color(0x9944F2C1) // 60% alpha
    val PrimaryGreenStatus = Color(0xFF4BC150)
    val PrimaryGreenDark = Color(0xFF299375)

    val PrimaryColor = PrimaryGreen

    val PrimaryBack = Color(0xFF1E2127)
    val PrimaryBackLight = Color(0xFF2C3039)
    val PrimaryDisable = Color(0xFF808080)
    val PrimaryRed = Color(0xFFDC0101)
    val PrimaryYellow = Color(0xFFF2C144)
    val PrimaryViolet = Color(0xFFA144F2)
    val PrimaryBlue = Color(0xFF44A1F2)
    val PrimaryGray = Color(0xFFB0B0B0)

    val SecondaryBack = PrimaryBackLight
    val PrimaryText = White
    val SecondaryText = PrimaryGray
    val Error = PrimaryRed

    // Material TextInput stroke color in Android resources aliases primary green
    val MtrlTextInputDefaultBoxStrokeColor = PrimaryGreen

    // spotlight background uses ARGB (#DF000000)
    val SpotlightBackground = Color(0xDF000000)

    val ColorScheme: ColorScheme = darkColorScheme(
        primary = PrimaryGreen,
        onPrimary = Black,
        primaryContainer = PrimaryGreenDark,
        onPrimaryContainer = White,
        inversePrimary = PrimaryGreenStatus,
        secondary = PrimaryBlue,
        onSecondary = Black,
        secondaryContainer = PrimaryBackLight,
        onSecondaryContainer = White,
        tertiary = PrimaryViolet,
        onTertiary = White,
        tertiaryContainer = PrimaryBack,
        onTertiaryContainer = White,
        background = PrimaryBack,
        onBackground = White,
        surface = PrimaryBackLight,
        onSurface = White,
        surfaceVariant = PrimaryGray,
        onSurfaceVariant = Black,
        inverseSurface = PrimaryGreen60,
        inverseOnSurface = Black,
        error = PrimaryRed,
        onError = White,
        errorContainer = PrimaryRed.copy(alpha = 0.12f), // Approximate low-opacity container
        onErrorContainer = PrimaryRed,
        outline = PrimaryDisable,
        outlineVariant = PrimaryGray,
        scrim = SpotlightBackground
    )
}
