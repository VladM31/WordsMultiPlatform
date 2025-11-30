package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme

@Composable
fun MatchWordCard(
    text: String,
    isSelected: Boolean,
    isMistake: Boolean,
    isMatched: Boolean,
    fontSize: TextUnit,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val containerColor = when {
        isMatched -> AppTheme.ColorScheme.primaryContainer.copy(alpha = 0.5f)
        isMistake -> AppTheme.ColorScheme.errorContainer
        isSelected -> AppTheme.ColorScheme.primary
        else -> AppTheme.ColorScheme.surface
    }

    val contentColor = when {
        isMatched -> AppTheme.ColorScheme.onSurface.copy(alpha = 0.8f)
        isMistake -> AppTheme.ColorScheme.error
        isSelected -> AppTheme.ColorScheme.onPrimary
        else -> AppTheme.ColorScheme.primary
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .then(if (enabled && !isMatched) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = LocalTextStyle.current.copy(lineHeight = fontSize * 1.1f),
            color = contentColor,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 12.dp)
                .fillMaxWidth()
        )
    }
}

