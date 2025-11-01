package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun BackHandler(navController: SimpleNavController) {
    // Call platform-specific back handler registration
    registerBackHandler(navController)

    // For non-Android platforms: cleanup event listeners
    DisposableEffect(navController) {
        val unregister = getBackHandlerCleanup(navController)
        onDispose {
            try {
                unregister()
            } catch (_: Throwable) {
            }
        }
    }
}

// Platform-specific registration: should add event listeners and return a lambda to remove them.
@Composable
expect fun registerBackHandler(navController: SimpleNavController)

// Get cleanup function for platforms that need it
expect fun getBackHandlerCleanup(navController: SimpleNavController): () -> Unit
