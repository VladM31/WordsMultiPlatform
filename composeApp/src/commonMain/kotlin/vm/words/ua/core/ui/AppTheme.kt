package vm.words.ua.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import vm.words.ua.core.ui.theme.ThemeManager

object AppTheme {
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)

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

    private val currentTheme
        get() = ThemeManager.instance.currentTheme.value

    val ColorScheme: ColorScheme
        get() = currentTheme.colorScheme
}
