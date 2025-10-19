@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package vm.words.ua.navigation

import kotlinx.browser.window
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

actual fun registerBackHandler(navController: SimpleNavController): () -> Unit {
    // popstate handler: when user clicks browser Back, pop nav
    val onPop: (Event) -> Unit = { _ ->
        try {
            navController.popBackStack()
        } catch (_: Throwable) {
        }
    }

    // keydown handler: Backspace, Alt+ArrowLeft, Escape
    val onKeyDown: (Event) -> Unit = { ev ->
        val ke = ev as KeyboardEvent
        val key = ke.key
        if (key == "Backspace" || (ke.altKey && key == "ArrowLeft") || key == "Escape") {
            try {
                val handled = navController.popBackStack()
                if (handled) ke.preventDefault()
            } catch (_: Throwable) {
            }
        }
    }

    // listener to push history entries on navigation so browser back works
    val navListener: (String) -> Unit = { route ->
        try {
            // push a new history state with hash route so URL changes but page doesn't reload
            window.history.pushState(null, "", "#${route}")
        } catch (_: Throwable) {
        }
    }

    window.addEventListener("popstate", onPop)
    window.addEventListener("keydown", onKeyDown)
    navController.addNavigateListener(navListener)

    return {
        try {
            window.removeEventListener("popstate", onPop)
            window.removeEventListener("keydown", onKeyDown)
            navController.removeNavigateListener(navListener)
        } catch (_: Throwable) {
        }
    }
}
