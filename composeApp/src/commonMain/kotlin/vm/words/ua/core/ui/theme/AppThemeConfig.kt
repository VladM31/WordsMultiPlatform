package vm.words.ua.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


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
        secondaryColor = Color(0xFFFFAB91), // Coral/peach for contrast
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
        secondaryColor = Color(0xFFFFD54F), // Golden/amber for contrast
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
        secondaryColor = Color(0xFF64B5F6), // Blue for contrast with orange
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
        secondaryColor = Color(0xFFFF8A80), // Coral pink for contrast
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
        secondaryColor = Color(0xFF4DD0E1), // Teal/cyan for contrast with pink
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
        secondaryColor = Color(0xFFFF5722), // Orange for contrast with blue
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
        secondaryColor = Color(0xFF00897B), // Teal for contrast with brown
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
        secondaryColor = Color(0xFFFF7043), // Orange for contrast
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
        secondaryColor = Color(0xFFFF7043), // Coral/orange for contrast with teal
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
        secondaryColor = Color(0xFFFFA726), // Amber/orange for contrast with purple
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
        secondaryColor = Color(0xFF26A69A), // Teal for contrast with orange
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
        secondaryColor = Color(0xFFEC407A), // Pink for contrast with green
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

    /** Neon Green - Electric green neon */
    val NeonGreen = AppThemeConfig(
        id = "neon_green",
        name = "Neon Green",
        isDark = true,
        primaryColor = Color(0xFF39FF14),  // Electric Green
        secondaryColor = Color(0xFFFF1493), // Deep Pink
        primaryBack = Color(0xFF0A0F0A),
        secondaryBack = Color(0xFF1A251A),
        primaryText = Color(0xFFE0FFE0),
        secondaryText = Color(0xFF90EE90),
        accentGreen = Color(0xFF00FF00),
        accentRed = Color(0xFFFF073A),
        accentYellow = Color(0xFFCCFF00),
        accentViolet = Color(0xFFFF00FF),
        accentBlue = Color(0xFF00FFFF)
    )

    /** Neon Orange - Hot orange neon */
    val NeonOrange = AppThemeConfig(
        id = "neon_orange",
        name = "Neon Orange",
        isDark = true,
        primaryColor = Color(0xFFFF6600),  // Neon Orange
        secondaryColor = Color(0xFF00FFFF), // Cyan
        primaryBack = Color(0xFF1A0A00),
        secondaryBack = Color(0xFF2D1500),
        primaryText = Color(0xFFFFE0CC),
        secondaryText = Color(0xFFFFB380),
        accentGreen = Color(0xFF39FF14),
        accentRed = Color(0xFFFF0000),
        accentYellow = Color(0xFFFFFF00),
        accentViolet = Color(0xFFFF00FF),
        accentBlue = Color(0xFF00B4FF)
    )

    /** Retro Sepia - Vintage brown tones */
    val RetroSepia = AppThemeConfig(
        id = "retro_sepia",
        name = "Retro Sepia",
        isDark = false,
        primaryColor = Color(0xFF8B4513),  // Saddle Brown
        secondaryColor = Color(0xFF2E7D32), // Forest green for contrast
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

    /** Yellow Light - Sunny yellow theme */
    val YellowLight = AppThemeConfig(
        id = "yellow_light",
        name = "Sunny Yellow",
        isDark = false,
        primaryColor = Color(0xFFF9A825),  // Amber/Yellow
        secondaryColor = Color(0xFF7B1FA2), // Purple for contrast
        primaryBack = Color(0xFFFFFDE7),   // Light Yellow
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF5D4037),
        secondaryText = Color(0xFF795548)
    )

    /** Yellow Dark - Golden dark theme */
    val YellowDark = AppThemeConfig(
        id = "yellow_dark",
        name = "Golden Night",
        isDark = true,
        primaryColor = Color(0xFFFFD600),  // Vivid Yellow
        secondaryColor = Color(0xFF7C4DFF), // Deep Purple for contrast
        primaryBack = Color(0xFF1A1A0A),
        secondaryBack = Color(0xFF2D2D15),
        primaryText = Color.White,
        secondaryText = Color(0xFFD4D4A0)
    )

    /** Galaxy - Deep space theme */
    val Galaxy = AppThemeConfig(
        id = "galaxy",
        name = "Galaxy",
        isDark = true,
        primaryColor = Color(0xFF9C27B0),  // Purple
        secondaryColor = Color(0xFF00BCD4), // Cyan
        primaryBack = Color(0xFF0D0D1A),   // Deep space blue
        secondaryBack = Color(0xFF1A1A2E),
        primaryText = Color(0xFFE8E8FF),
        secondaryText = Color(0xFFB8B8D0),
        accentGreen = Color(0xFF00E676),
        accentRed = Color(0xFFFF5252),
        accentYellow = Color(0xFFFFD740),
        accentViolet = Color(0xFFE040FB),
        accentBlue = Color(0xFF40C4FF)
    )

    /** Autumn - Warm fall colors */
    val Autumn = AppThemeConfig(
        id = "autumn",
        name = "Autumn",
        isDark = false,
        primaryColor = Color(0xFFBF360C),  // Deep Orange
        secondaryColor = Color(0xFF1B5E20), // Dark Green for contrast
        primaryBack = Color(0xFFFFF3E0),   // Light Orange
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF3E2723),
        secondaryText = Color(0xFF5D4037),
        accentGreen = Color(0xFF558B2F),
        accentRed = Color(0xFFC62828),
        accentYellow = Color(0xFFF9A825),
        accentViolet = Color(0xFF6A1B9A),
        accentBlue = Color(0xFF0277BD)
    )

    /** Cherry Blossom - Japanese sakura theme */
    val CherryBlossom = AppThemeConfig(
        id = "cherry_blossom",
        name = "Cherry Blossom",
        isDark = false,
        primaryColor = Color(0xFFE91E63),  // Pink
        secondaryColor = Color(0xFF4CAF50), // Green for contrast
        primaryBack = Color(0xFFFCE4EC),   // Light Pink
        secondaryBack = Color(0xFFFFFFFF),
        primaryText = Color(0xFF880E4F),
        secondaryText = Color(0xFFC2185B)
    )

    /** Northern Lights - Aurora theme */
    val NorthernLights = AppThemeConfig(
        id = "northern_lights",
        name = "Northern Lights",
        isDark = true,
        primaryColor = Color(0xFF00E5FF),  // Cyan
        secondaryColor = Color(0xFFAA00FF), // Purple
        primaryBack = Color(0xFF001524),   // Dark blue
        secondaryBack = Color(0xFF002233),
        primaryText = Color(0xFFE0FFFF),
        secondaryText = Color(0xFF80DEEA),
        accentGreen = Color(0xFF00FF7F),   // Spring Green
        accentRed = Color(0xFFFF6B6B),
        accentYellow = Color(0xFFFFE66D),
        accentViolet = Color(0xFFDA70D6),  // Orchid
        accentBlue = Color(0xFF00BFFF)     // Deep Sky Blue
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
        YellowDark,
        Galaxy,
        NorthernLights,
        // Light themes
        CleanWhite,
        CreamLight,
        SkyBlueLight,
        MintFresh,
        LavenderLight,
        PeachLight,
        SageGreen,
        YellowLight,
        Autumn,
        CherryBlossom,
        // Special/Neon themes
        NeonCyberpunk,
        NeonGreen,
        NeonOrange,
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

