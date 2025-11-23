package vm.words.ua.hints.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize

@Composable
fun HintBubble(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(48.dp)
            .background(AppTheme.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 32.dp, vertical = 24.dp),
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                color = AppTheme.White,
                textAlign = TextAlign.Center,
                fontSize = getFontSize() * 1.3f,
                lineHeight = getFontSize() * 1.4f
            ),
        )
    }
}