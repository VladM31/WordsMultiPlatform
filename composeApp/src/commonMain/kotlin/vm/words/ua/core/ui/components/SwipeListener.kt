package vm.words.ua.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import vm.words.ua.navigation.LocalSwipeDownOverride
import vm.words.ua.navigation.LocalSwipeLeftOverride
import vm.words.ua.navigation.LocalSwipeRightOverride
import vm.words.ua.navigation.LocalSwipeUpOverride

@Composable
fun SwipeListener(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null
) {
    val swipeRightOverride = LocalSwipeRightOverride.current
    val swipeLeftOverride = LocalSwipeLeftOverride.current
    val swipeUpOverride = LocalSwipeUpOverride.current
    val swipeDownOverride = LocalSwipeDownOverride.current

    DisposableEffect(Unit) {
        onDispose {
            swipeRightOverride.value = null
            swipeLeftOverride.value = null
            swipeUpOverride.value = null
            swipeDownOverride.value = null
        }
    }

    LaunchedEffect(onSwipeRight) { swipeRightOverride.value = onSwipeRight }
    LaunchedEffect(onSwipeLeft) { swipeLeftOverride.value = onSwipeLeft }
    LaunchedEffect(onSwipeUp) { swipeUpOverride.value = onSwipeUp }
    LaunchedEffect(onSwipeDown) { swipeDownOverride.value = onSwipeDown }
}