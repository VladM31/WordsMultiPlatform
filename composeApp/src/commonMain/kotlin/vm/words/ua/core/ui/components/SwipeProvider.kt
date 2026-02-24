package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.kodein.di.instance
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.LocalSwipeDownOverride
import vm.words.ua.navigation.LocalSwipeLeftOverride
import vm.words.ua.navigation.LocalSwipeRightOverride
import vm.words.ua.navigation.LocalSwipeUpOverride
import vm.words.ua.navigation.SimpleNavController

@Composable
fun SwipeProvider(
    route: String,
    content: @Composable () -> Unit
) {
    val navController: SimpleNavController by DiContainer.di.instance()

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

    val onSwipeRight: (() -> Unit)? = swipeRightOverride.value
        ?: if (!navController.isLastScreen) ({ navController.popBackStack() }) else null

    CompositionLocalProvider(
        LocalSwipeRightOverride provides swipeRightOverride,
        LocalSwipeLeftOverride provides swipeLeftOverride,
        LocalSwipeUpOverride provides swipeUpOverride,
        LocalSwipeDownOverride provides swipeDownOverride,
    ) {
        SwipeHandler(
            modifier = Modifier.fillMaxSize(),
            onSwipeRight = onSwipeRight,
            onSwipeLeft = swipeLeftOverride.value,
            onSwipeUp = swipeUpOverride.value,
            onSwipeDown = swipeDownOverride.value,
            content = { content() }
        )
    }
}