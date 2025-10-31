package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme

@Composable
fun OptionCard(
    text: String,
    isRight: Boolean,
    isWrong: Boolean,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val containerColor =
        when {
            isRight -> AppTheme.ColorSchema.primary
            isWrong -> AppTheme.ColorSchema.errorContainer
            else -> AppTheme.ColorSchema.surface
        }

    val contentColor =
        when {
            isRight -> AppTheme.ColorSchema.onPrimary
            isWrong -> AppTheme.ColorSchema.error
            else -> AppTheme.ColorSchema.primary
        }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = contentColor,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 12.dp)
                .fillMaxWidth()
        )
    }
}