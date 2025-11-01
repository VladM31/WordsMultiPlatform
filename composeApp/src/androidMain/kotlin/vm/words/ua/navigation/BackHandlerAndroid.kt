package vm.words.ua.navigation

import androidx.activity.compose.BackHandler as AndroidBackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun registerBackHandler(navController: SimpleNavController) {
    AndroidBackHandler(enabled = navController.isLastScreen.not()) {
        navController.popBackStack()
    }
}

actual fun getBackHandlerCleanup(navController: SimpleNavController): () -> Unit {
    // Android BackHandler is managed by Compose, no cleanup needed
    return { }
}
