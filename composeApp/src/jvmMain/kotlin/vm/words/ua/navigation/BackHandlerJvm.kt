package vm.words.ua.navigation

import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

actual fun registerBackHandler(navController: SimpleNavController): () -> Unit {
    val dispatcher = KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .createKeyEventDispatcher { e ->
            try {
                if (e.id == KeyEvent.KEY_PRESSED) {
                    val keyCode = e.keyCode
                    val isAlt = e.isAltDown
                    if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_ESCAPE || (isAlt && keyCode == KeyEvent.VK_LEFT)) {
                        val handled = navController.popBackStack()
                        if (handled) {
                            e.consume()
                        }
                    }
                }
            } catch (_: Throwable) {
            }
            false
        }

    // KeyboardFocusManager doesn't provide a direct remove for created dispatchers; we'll add dispatcher and return a remove function
    val kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager()
    kfm.addKeyEventDispatcher(dispatcher)

    return {
        try {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher)
        } catch (_: Throwable) {
        }
    }
}

// Helper to create a KeyEventDispatcher from a lambda
private fun KeyboardFocusManager.createKeyEventDispatcher(handler: (KeyEvent) -> Boolean): java.awt.KeyEventDispatcher {
    return java.awt.KeyEventDispatcher { e -> handler(e) }
}

