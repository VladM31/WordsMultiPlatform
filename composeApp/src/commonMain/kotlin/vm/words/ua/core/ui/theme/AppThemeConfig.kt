package vm.words.ua.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Represents a theme configuration for the app
 */
data class AppThemeConfig(
    val id: String,
    val name: String,
    val isDark: Boolean,
    val primaryColor: Color,
    val secondaryColor: Color,
    val primaryBack: Color,
    val secondaryBack: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val accentGreen: Color = Color(0xFF4BC150),
    val accentRed: Color = Color(0xFFDC0101),
    val accentYellow: Color = Color(0xFFF2C144),
    val accentViolet: Color = Color(0xFFA144F2),
    val accentBlue: Color = Color(0xFF44A1F2),
    val disabledColor: Color = Color(0xFF808080)
) {
    val colorScheme: ColorScheme
        get() = if (isDark) {
            darkColorScheme(
                primary = primaryColor,
                onPrimary = if (isDark) Color.Black else Color.White,
                primaryContainer = primaryColor.copy(alpha = 0.7f),
                onPrimaryContainer = Color.White,
                inversePrimary = accentGreen,
                secondary = secondaryColor,
                onSecondary = Color.Black,
                secondaryContainer = secondaryBack,
                onSecondaryContainer = Color.White,
                tertiary = accentViolet,
                onTertiary = Color.White,
                tertiaryContainer = primaryBack,
                onTertiaryContainer = Color.White,
                background = primaryBack,
                onBackground = primaryText,
                surface = secondaryBack,
                onSurface = primaryText,
                surfaceVariant = secondaryText,
                onSurfaceVariant = Color.Black,
                inverseSurface = primaryColor.copy(alpha = 0.60f),
                inverseOnSurface = Color.Black,
                error = accentRed,
                onError = Color.White,
                errorContainer = accentRed.copy(alpha = 0.12f),
                onErrorContainer = accentRed,
                outline = disabledColor,
                outlineVariant = secondaryText,
                scrim = Color(0xDF000000)
            )
        } else {
            lightColorScheme(
                primary = primaryColor,
                onPrimary = Color.White,
                primaryContainer = primaryColor.copy(alpha = 0.2f),
                onPrimaryContainer = primaryColor,
                inversePrimary = accentGreen,
                secondary = secondaryColor,
                onSecondary = Color.White,
                secondaryContainer = secondaryBack,
                onSecondaryContainer = Color.Black,
                tertiary = accentViolet,
                onTertiary = Color.White,
                tertiaryContainer = primaryBack,
                onTertiaryContainer = Color.Black,
                background = primaryBack,
                onBackground = primaryText,
                surface = secondaryBack,
                onSurface = primaryText,
                surfaceVariant = secondaryText,
                onSurfaceVariant = Color.White,
                inverseSurface = primaryColor.copy(alpha = 0.60f),
                inverseOnSurface = Color.White,
                error = accentRed,
                onError = Color.White,
                errorContainer = accentRed.copy(alpha = 0.12f),
                onErrorContainer = accentRed,
                outline = disabledColor,
                outlineVariant = secondaryText,
                scrim = Color(0x8F000000)
            )
        }
}

/**
 * All available themes in the app
 */
object AppThemes {

    // ===== DARK THEMES =====

    /** Default dark theme - Cyan/Teal accent */
    val Default = AppThemeConfig(
        id = "default",
        name = "Default Dark",
        isDark = true,
        primaryColor = Color(0xFF44F2C1),
        secondaryColor = Color(0xFFA144F2),
        primaryBack = Color(0xFF1E2127),
        secondaryBack = Color(0xFF2C3039),
        primaryText = Color.White,
        secondaryText = Color(0xFFB0B0B0)
    )

    /** Ocean Night - Deep blue theme */
    val OceanNight = AppThemeConfig(
        id = "ocean_night",
        name = "Ocean Night",
        isDark = true,
        primaryColor = Color(0xFF64B5F6),
        secondaryColor = Color(0xFF4DD0E1),
        primaryBack = Color(0xFF0D1B2A),
        secondaryBack = Color(0xFF1B263B),
        primaryText = Color.White,
        secondaryText = Color(0xFF8EACCD)
    )

    /** Purple Dream - Violet accent */
    val PurpleDream = AppThemeConfig(
        id = "purple_dream",
        name = "Purple Dream",
        isDark = true,
        primaryColor = Color(0xFFBB86FC),
        secondaryColor = Color(0xFF03DAC6),
        primaryBack = Color(0xFF121212),
        secondaryBack = Color(0xFF1E1E1E),
        primaryText = Color.White,
        secondaryText = Color(0xFFB3B3B3)
    )

    /** Forest Night - Green accent */
    val ForestNight = AppThemeConfig(
        id = "forest_night",
        name = "Forest Night",
        isDark = true,
        primaryColor = Color(0xFF81C784),
        secondaryColor = Color(0xFFA5D6A7),
        primaryBack = Color(0xFF1A2F1A),
        secondaryBack = Color(0xFF2D4A2D),
        primaryText = Color.White,
        secondaryText = Color(0xFF9CCC9C)
    )

    /** Sunset Dark - Orange/Red accent */
    val SunsetDark = AppThemeConfig(
        id = "sunset_dark",
        name = "Sunset Dark",
        isDark = true,
        primaryColor = Color(0xFFFF7043),
        secondaryColor = Color(0xFFFFAB91),
        primaryBack = Color(0xFF1F1410),
        secondaryBack = Color(0xFF2D1F1A),
        primaryText = Color.White,
        secondaryText = Color(0xFFCCAA99)
    )

    /** Midnight Blue */
    val MidnightBlue = AppThemeConfig(
        id = "midnight_blue",
        name = "Midnight Blue",
        isDark = true,
        primaryColor = Color(0xFF5C6BC0),
        secondaryColor = Color(0xFF7986CB),
        primaryBack = Color(0xFF0A0E1A),
        secondaryBack = Color(0xFF151B30),
        primaryText = Color.White,
        secondaryText = Color(0xFF9FA8DA)
    )

    /** Rose Gold Dark */
    val RoseGoldDark = AppThemeConfig(
        id = "rose_gold_dark",
        name = "Rose Gold",
        isDark = true,
        primaryColor = Color(0xFFF48FB1),
        secondaryColor = Color(0xFFCE93D8),
        primaryBack = Color(0xFF1A1215),
        secondaryBack = Color(0xFF2A1F25),
        primaryText = Color.White,
        secondaryText = Color(0xFFD4A5B5)
    )

    // ===== LIGHT THEMES =====

    /** Clean White - Minimal light theme */
    val CleanWhite = AppThemeConfig(
        id = "clean_white",
        name = "Clean White",
        isDark = false,
        primaryColor = Color(0xFF2196F3),
        secondaryColor = Color(0xFF03A9F4),
        primaryBack = Color(0xFFFAFAFA),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF212121),
        secondaryText = Color(0xFF757575)
    )

    /** Cream Light - Warm light theme */
    val CreamLight = AppThemeConfig(
        id = "cream_light",
        name = "Cream Light",
        isDark = false,
        primaryColor = Color(0xFF795548),
        secondaryColor = Color(0xFFA1887F),
        primaryBack = Color(0xFFFFF8E1),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF3E2723),
        secondaryText = Color(0xFF6D4C41)
    )

    /** Sky Blue Light */
    val SkyBlueLight = AppThemeConfig(
        id = "sky_blue_light",
        name = "Sky Blue",
        isDark = false,
        primaryColor = Color(0xFF0288D1),
        secondaryColor = Color(0xFF4FC3F7),
        primaryBack = Color(0xFFE3F2FD),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF01579B),
        secondaryText = Color(0xFF0277BD)
    )

    /** Mint Fresh Light */
    val MintFresh = AppThemeConfig(
        id = "mint_fresh",
        name = "Mint Fresh",
        isDark = false,
        primaryColor = Color(0xFF00897B),
        secondaryColor = Color(0xFF4DB6AC),
        primaryBack = Color(0xFFE0F2F1),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF004D40),
        secondaryText = Color(0xFF00695C)
    )

    /** Lavender Light */
    val LavenderLight = AppThemeConfig(
        id = "lavender_light",
        name = "Lavender",
        isDark = false,
        primaryColor = Color(0xFF7E57C2),
        secondaryColor = Color(0xFFB39DDB),
        primaryBack = Color(0xFFF3E5F5),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF4A148C),
        secondaryText = Color(0xFF6A1B9A)
    )

    /** Peach Light */
    val PeachLight = AppThemeConfig(
        id = "peach_light",
        name = "Peach",
        isDark = false,
        primaryColor = Color(0xFFE64A19),
        secondaryColor = Color(0xFFFF8A65),
        primaryBack = Color(0xFFFBE9E7),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFFBF360C),
        secondaryText = Color(0xFFD84315)
    )

    /** Sage Green Light */
    val SageGreen = AppThemeConfig(
        id = "sage_green",
        name = "Sage Green",
        isDark = false,
        primaryColor = Color(0xFF558B2F),
        secondaryColor = Color(0xFF8BC34A),
        primaryBack = Color(0xFFF1F8E9),
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF33691E),
        secondaryText = Color(0xFF689F38)
    )

    // ===== SPECIAL/UNUSUAL THEMES =====

    /** Neon Cyberpunk - Vibrant neon colors */
    val NeonCyberpunk = AppThemeConfig(
        id = "neon_cyberpunk",
        name = "Neon Cyberpunk",
        isDark = true,
        primaryColor = Color(0xFFFF00FF),  // Magenta
        secondaryColor = Color(0xFF00FFFF), // Cyan
        primaryBack = Color(0xFF0D0221),
        secondaryBack = Color(0xFF190535),
        primaryText = Color(0xFFE0E0FF),
        secondaryText = Color(0xFFB39DDB),
        accentGreen = Color(0xFF39FF14),
        accentRed = Color(0xFFFF073A),
        accentYellow = Color(0xFFFFFF00),
        accentViolet = Color(0xFFBF00FF),
        accentBlue = Color(0xFF00F0FF)
    )

    /** Retro Sepia - Vintage brown tones */
    val RetroSepia = AppThemeConfig(
        id = "retro_sepia",
        name = "Retro Sepia",
        isDark = false,
        primaryColor = Color(0xFF8B4513),  // Saddle Brown
        secondaryColor = Color(0xFFD2691E), // Chocolate
        primaryBack = Color(0xFFF5E6D3),   // Antique White
        secondaryBack = Color(0xFFFAEBD7),
        primaryText = Color(0xFF3E2723),
        secondaryText = Color(0xFF5D4037),
        accentGreen = Color(0xFF6B8E23),   // Olive
        accentRed = Color(0xFF8B0000),     // Dark Red
        accentYellow = Color(0xFFDAA520),  // Goldenrod
        accentViolet = Color(0xFF800080),  // Purple
        accentBlue = Color(0xFF4682B4)     // Steel Blue
    )

    /** All available themes */
    val allThemes = listOf(
        // Dark themes
        Default,
        OceanNight,
        PurpleDream,
        ForestNight,
        SunsetDark,
        MidnightBlue,
        RoseGoldDark,
        // Light themes
        CleanWhite,
        CreamLight,
        SkyBlueLight,
        MintFresh,
        LavenderLight,
        PeachLight,
        SageGreen,
        // Special themes
        NeonCyberpunk,
        RetroSepia
    )

    /** Get theme by ID, fallback to default */
    fun getById(id: String): AppThemeConfig {
        return allThemes.find { it.id == id } ?: Default
    }

    /** Dark themes only */
    val darkThemes = allThemes.filter { it.isDark }

    /** Light themes only */
    val lightThemes = allThemes.filter { !it.isDark }
}

