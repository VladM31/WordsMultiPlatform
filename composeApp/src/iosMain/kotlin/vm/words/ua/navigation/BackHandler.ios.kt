package vm.words.ua.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun registerBackHandler(navController: SimpleNavController) {
    // iOS has no system back button for our SwiftUI host; no-op
}

actual fun getBackHandlerCleanup(navController: SimpleNavController): () -> Unit = { }

