package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun BackHandler(navController: SimpleNavController) {
    DisposableEffect(navController) {
        val unregister = registerBackHandler(navController)
        onDispose {
            try {
                unregister()
            } catch (_: Throwable) {
            }
        }
    }
}

// Platform-specific registration: should add event listeners and return a lambda to remove them.
expect fun registerBackHandler(navController: SimpleNavController): () -> Unit

