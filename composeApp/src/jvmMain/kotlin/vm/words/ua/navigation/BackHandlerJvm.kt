package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

private var jvmDispatcher: java.awt.KeyEventDispatcher? = null

@Composable
actual fun registerBackHandler(navController: SimpleNavController) {
    if (jvmDispatcher != null) return // Already registered

    jvmDispatcher = KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .createKeyEventDispatcher { e ->
            try {
                if (e.id != KeyEvent.KEY_PRESSED){
                    return@createKeyEventDispatcher false
                }
                if (navController.isLastScreen){
                    return@createKeyEventDispatcher false
                }
                val keyCode = e.keyCode
                val isAlt = e.isAltDown
                if (keyCode == KeyEvent.VK_ESCAPE || (isAlt && keyCode == KeyEvent.VK_LEFT)) {
                    val handled = navController.popBackStack()
                    if (handled) {
                        e.consume()
                    }
                }
            } catch (_: Throwable) {
            }
            false
        }

    // KeyboardFocusManager doesn't provide a direct remove for created dispatchers; we'll add dispatcher and return a remove function
    val kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager()
    kfm.addKeyEventDispatcher(jvmDispatcher)
}

actual fun getBackHandlerCleanup(navController: SimpleNavController): () -> Unit {
    return {
        val dispatcher = jvmDispatcher
        if (dispatcher != null) {
            try {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher)
                jvmDispatcher = null
            } catch (_: Throwable) {
            }
        }
    }
}

// Helper to create a KeyEventDispatcher from a lambda
private fun KeyboardFocusManager.createKeyEventDispatcher(handler: (KeyEvent) -> Boolean): java.awt.KeyEventDispatcher {
    return java.awt.KeyEventDispatcher { e -> handler(e) }
}
