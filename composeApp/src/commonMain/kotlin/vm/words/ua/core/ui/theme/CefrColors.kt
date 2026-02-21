package vm.words.ua.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.ui.AppTheme

@Composable
fun CEFR.toColor(): Color {
    return when (this) {
        // Use a slightly toned-down primary color for A1 (beginner) so it's visually distinct but still consistent
        CEFR.A1 -> AppTheme.PrimaryGreen
        CEFR.A2 -> AppTheme.PrimaryBlue
        CEFR.B1 -> AppTheme.PrimaryViolet
        CEFR.B2 -> AppTheme.PrimaryYellow
        CEFR.C1 -> AppTheme.PrimaryOrange
        CEFR.C2 -> AppTheme.PrimaryRed
    }
}
