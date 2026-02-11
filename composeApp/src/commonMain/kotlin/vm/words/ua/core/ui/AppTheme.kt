package vm.words.ua.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import vm.words.ua.core.ui.theme.AppThemeConfig
import vm.words.ua.core.ui.theme.ThemeManager

object AppTheme {
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)

    // Static accessors (for non-composable code) - these won't trigger recomposition
    val PrimaryGreen: Color
        get() = currentTheme.accentGreen

    val PrimaryDisable: Color
        get() = currentTheme.disabledColor

    val PrimaryRed: Color
        get() = currentTheme.accentRed

    val PrimaryYellow: Color
        get() = currentTheme.accentYellow

    val PrimaryViolet: Color
        get() = currentTheme.accentViolet

    val PrimaryBlue: Color
        get() = currentTheme.accentBlue

    val PrimaryGray: Color
        get() = currentTheme.secondaryText

    val PrimaryColor: Color
        get() = currentTheme.primaryColor

    val PrimaryColorDark: Color
        get() = currentTheme.primaryColor.copy(alpha = 0.7f)

    val SecondaryColor: Color
        get() = currentTheme.secondaryColor

    val PrimaryBack: Color
        get() = currentTheme.primaryBack

    val SecondaryBack: Color
        get() = currentTheme.secondaryBack

    val PrimaryText: Color
        get() = currentTheme.primaryText

    val SecondaryText: Color
        get() = currentTheme.secondaryText

    val Error: Color
        get() = currentTheme.accentRed

    // spotlight background uses ARGB (#DF000000)
    val SpotlightBackground = Color(0xDF000000)

    private val currentTheme
        get() = ThemeManager.instance.currentTheme.value

    val ColorScheme: ColorScheme
        get() = currentTheme.colorScheme
}

/**
 * Composable color provider that triggers recomposition when theme changes.
 * Use this in Composable functions to get reactive theme colors.
 */
object AppColors {

    private val theme: AppThemeConfig
        @Composable
        get() {
            val t by ThemeManager.instance.currentTheme.collectAsState()
            return t
        }

    val primaryColor: Color
        @Composable
        get() = theme.primaryColor

    val primaryColorDark: Color
        @Composable
        get() = theme.primaryColor.copy(alpha = 0.7f)

    val secondaryColor: Color
        @Composable
        get() = theme.secondaryColor

    val primaryBack: Color
        @Composable
        get() = theme.primaryBack

    val secondaryBack: Color
        @Composable
        get() = theme.secondaryBack

    val primaryText: Color
        @Composable
        get() = theme.primaryText

    val secondaryText: Color
        @Composable
        get() = theme.secondaryText

    val primaryGreen: Color
        @Composable
        get() = theme.accentGreen

    val primaryRed: Color
        @Composable
        get() = theme.accentRed

    val primaryYellow: Color
        @Composable
        get() = theme.accentYellow

    val primaryViolet: Color
        @Composable
        get() = theme.accentViolet

    val primaryBlue: Color
        @Composable
        get() = theme.accentBlue

    val primaryDisable: Color
        @Composable
        get() = theme.disabledColor

    val primaryGray: Color
        @Composable
        get() = theme.secondaryText

    val error: Color
        @Composable
        get() = theme.accentRed
}

