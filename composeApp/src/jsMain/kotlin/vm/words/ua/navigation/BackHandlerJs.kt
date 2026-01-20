package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import kotlinx.browser.window
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

private var jsCleanup: (() -> Unit)? = null

// Track our own history index stored in history.state (Int)
private var currentHistoryIndexJs: Int = 0

@Composable
actual fun registerBackHandler(navController: SimpleNavController) {
    if (jsCleanup != null) return // Already registered

    // Ensure history.state is initialized with an index so we can detect direction
    try {
        val st: Any? = window.history.state
        if (st == null) {
            window.history.replaceState(0, "", window.location.href)
            currentHistoryIndexJs = 0
        } else {
            currentHistoryIndexJs = (st as? Int) ?: 0
        }
    } catch (_: Throwable) {
        currentHistoryIndexJs = 0
    }

    // popstate handler: when user clicks browser nav, detect direction
    val onPop: (Event) -> Unit = { _ ->
        try {
            val newIndex = (window.history.state as? Int) ?: 0
            when {
                newIndex > currentHistoryIndexJs -> {
                    // Forward attempt: block and go back
                    window.history.go(-1)
                }
                newIndex < currentHistoryIndexJs -> {
                    // Back navigation
                    currentHistoryIndexJs = newIndex
                    navController.popBackStack()
                }
                else -> {
                    // Same index, ignore
                }
            }
        } catch (_: Throwable) {
        }
    }

    // keydown handler: Backspace, Alt+ArrowLeft, Escape; block forward shortcuts too
    val onKeyDown: (Event) -> Unit = { ev ->
        val ke = ev as KeyboardEvent
        val key = ke.key
        if ((ke.altKey && key == "ArrowLeft") || key == "Escape") {
            try {
                if (navController.isLastScreen.not()){
                    val handled = navController.popBackStack()
                    if (handled) ke.preventDefault()
                }

            } catch (_: Throwable) {
            }
        }
        if ((ke.altKey && key == "ArrowRight") || key == "BrowserForward") {
            try { ke.preventDefault() } catch (_: Throwable) {}
        }
    }

    // listener to push history entries on navigation so browser back works
    val navListener: (String) -> Unit = { route ->
        try {
            currentHistoryIndexJs += 1
            window.history.pushState(currentHistoryIndexJs, "", "#" + route)
        } catch (_: Throwable) {
        }
    }

    window.addEventListener("popstate", onPop)
    window.addEventListener("keydown", onKeyDown)
    navController.addNavigateListener(navListener)

    jsCleanup = {
        try {
            window.removeEventListener("popstate", onPop)
            window.removeEventListener("keydown", onKeyDown)
            navController.removeNavigateListener(navListener)
        } catch (_: Throwable) {
        }
    }
}

actual fun getBackHandlerCleanup(navController: SimpleNavController): () -> Unit {
    return jsCleanup ?: { }
}
