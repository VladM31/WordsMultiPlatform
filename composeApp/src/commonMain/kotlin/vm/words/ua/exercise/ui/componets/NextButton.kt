package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize

private val BUTTON_PADDING = 16.dp
private const val LINE_HEIGHT_FACTOR: Float = 1.2f

@Composable
fun calcNextButtonHeight(): Dp {
    val fontSize = getFontSize()
    return fontSize.value.dp * LINE_HEIGHT_FACTOR + BUTTON_PADDING * 2
}

@Composable
fun NextButton(
    hide: Boolean,
    text: String = "Next",
    onNextClick: () -> Unit
){
    if (hide) {
        return
    }
    val fontSize = getFontSize()

    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(Modifier.background(AppTheme.PrimaryBack).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
            Button(onClick = onNextClick, modifier = Modifier.padding(BUTTON_PADDING)) {
                Text(text, fontSize = fontSize, modifier = Modifier.padding(horizontal = 32.dp))
            }
        }
    }
}