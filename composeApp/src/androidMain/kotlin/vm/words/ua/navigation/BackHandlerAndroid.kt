package vm.words.ua.navigation

// On Android we rely on the system back and can enhance later via androidx.activity.compose.BackHandler.
// For now, no-op to allow compilation.
actual fun registerBackHandler(navController: SimpleNavController): () -> Unit = { }

