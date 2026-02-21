package vm.words.ua.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.ui.AppTheme

@Composable
fun CEFR.toColor(): Color {
    return when (this) {
        CEFR.A1 -> AppTheme.PrimaryColor
        CEFR.A2 -> AppTheme.PrimaryGreen
        CEFR.B1 -> AppTheme.PrimaryBlue
        CEFR.B2 -> AppTheme.PrimaryViolet
        CEFR.C1 -> AppTheme.PrimaryYellow
        CEFR.C2 -> AppTheme.PrimaryRed
    }
}
