package vm.words.ua.core.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize

@Composable
fun CenteredLoader(
    modifier: Modifier = Modifier,
    message: String? = null,
    size: Dp = 48.dp
) {
    val fontSize = getFontSize()

    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnim = infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .alpha(alphaAnim.value),
            color = AppTheme.PrimaryColor
        )

        if (!message.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = AppTheme.PrimaryColor,
                fontSize = fontSize
            )
        }
    }
}

