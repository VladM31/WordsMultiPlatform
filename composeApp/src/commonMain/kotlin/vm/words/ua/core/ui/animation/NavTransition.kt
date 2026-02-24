package vm.words.ua.core.ui.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val ENTER_DURATION = 220
private const val EXIT_DURATION = 160
private const val SCALE_INITIAL = 0.94f
private const val SCALE_TARGET = 0.97f

private data class NavState(val route: String, val isBack: Boolean)

@Composable
fun NavTransition(
    route: String,
    isNavigatingBack: Boolean,
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable (currentRoute: String) -> Unit
) {
    val enterSpec = tween<Float>(durationMillis = ENTER_DURATION, easing = FastOutSlowInEasing)
    val exitSpec = tween<Float>(durationMillis = EXIT_DURATION, easing = FastOutLinearInEasing)

    AnimatedContent(
        targetState = NavState(route, isNavigatingBack),
        transitionSpec = {
            val isBack = targetState.isBack
            if (isBack) {
                fadeIn(enterSpec) togetherWith
                        (fadeOut(exitSpec) + scaleOut(exitSpec, targetScale = SCALE_TARGET))
            } else {
                (fadeIn(enterSpec) + scaleIn(enterSpec, initialScale = SCALE_INITIAL)) togetherWith
                        fadeOut(exitSpec)
            }
        },
        modifier = modifier,
        content = { content(it.route) }
    )
}