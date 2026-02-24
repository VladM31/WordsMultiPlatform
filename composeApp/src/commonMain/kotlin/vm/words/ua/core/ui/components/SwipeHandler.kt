package vm.words.ua.core.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.core.ui.screen.UpdateScreen
import vm.words.ua.navigation.LocalSwipeDownOverride
import vm.words.ua.navigation.LocalSwipeLeftOverride
import vm.words.ua.navigation.LocalSwipeRightOverride
import vm.words.ua.navigation.LocalSwipeUpOverride
import vm.words.ua.navigation.Screen
import kotlin.math.abs

private const val SWIPE_THRESHOLD = 50f

@Composable
fun SwipeProvider(
    route: String,
    content: @Composable BoxScope.() -> Unit
) {
    val swipeRightOverride = remember { mutableStateOf<(() -> Unit)?>(null) }
    val swipeLeftOverride = remember { mutableStateOf<(() -> Unit)?>(null) }
    val swipeUpOverride = remember { mutableStateOf<(() -> Unit)?>(null) }
    val swipeDownOverride = remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(route) {
        swipeRightOverride.value = null
        swipeLeftOverride.value = null
        swipeUpOverride.value = null
        swipeDownOverride.value = null
    }

    CompositionLocalProvider(
        LocalSwipeRightOverride provides swipeRightOverride,
        LocalSwipeLeftOverride provides swipeLeftOverride,
        LocalSwipeUpOverride provides swipeUpOverride,
        LocalSwipeDownOverride provides swipeDownOverride
    ) {
        SwipeHandler(
            modifier = Modifier.fillMaxSize(),
            onSwipeRight = swipeRightOverride.value,
            onSwipeLeft = swipeLeftOverride.value,
            onSwipeUp = swipeUpOverride.value,
            onSwipeDown = swipeDownOverride.value,
            content = content
        )
    }
}

@Composable
fun SwipeHandler(
    modifier: Modifier = Modifier,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.pointerInput(onSwipeLeft, onSwipeRight, onSwipeUp, onSwipeDown) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                var totalX = 0f
                var totalY = 0f

                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull { it.id == down.id } ?: break
                    if (!change.pressed) break
                    totalX += change.positionChange().x
                    totalY += change.positionChange().y
                }

                val absX = abs(totalX)
                val absY = abs(totalY)

                if (absX > absY && absX > SWIPE_THRESHOLD) {
                    if (totalX > 0) onSwipeRight?.invoke()
                    else onSwipeLeft?.invoke()
                } else if (absY > absX && absY > SWIPE_THRESHOLD) {
                    if (totalY > 0) onSwipeDown?.invoke()
                    else onSwipeUp?.invoke()
                }
            }
        },
        content = content
    )
}