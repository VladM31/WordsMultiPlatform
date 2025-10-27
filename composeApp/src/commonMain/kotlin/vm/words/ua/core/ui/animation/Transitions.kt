package vm.words.ua.core.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset

// Cross-platform shims for intuitive naming
// slideInUp: item appears sliding from bottom to its position
fun slideInUp(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 300),
    distanceProvider: (fullHeight: Int) -> Int = { it }
): EnterTransition = slideInVertically(
    initialOffsetY = distanceProvider,
    animationSpec = animationSpec
)

// slideOutDown: item disappears sliding down off the screen
fun slideOutDown(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 300),
    distanceProvider: (fullHeight: Int) -> Int = { it }
): ExitTransition = slideOutVertically(
    targetOffsetY = distanceProvider,
    animationSpec = animationSpec
)
