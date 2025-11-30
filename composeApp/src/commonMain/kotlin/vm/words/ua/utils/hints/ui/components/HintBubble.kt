package vm.words.ua.utils.hints.ui.components

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
import vm.words.ua.core.utils.rememberFontSize

@Composable
fun HintBubble(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(24.dp)
            .background(AppTheme.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                color = AppTheme.White,
                textAlign = TextAlign.Center,
                fontSize = rememberFontSize() * 1.3f,
                lineHeight = rememberFontSize() * 1.4f
            ),
        )
    }
}