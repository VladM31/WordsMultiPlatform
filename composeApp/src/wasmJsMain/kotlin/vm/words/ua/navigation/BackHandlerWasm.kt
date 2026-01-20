@file:OptIn(ExperimentalWasmJsInterop::class)
package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import kotlinx.browser.window
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

private var wasmCleanup: (() -> Unit)? = null

// Track our own history index stored in history.state (Int)
private var currentHistoryIndexWasm: Int = 0

@Composable
actual fun registerBackHandler(navController: SimpleNavController) {
    if (wasmCleanup != null) return // Already registered

    // Ensure history.state is initialized with an index so we can detect direction
    try {
        val st: Any? = window.history.state
        if (st == null) {
            // Put index 0 into current URL without adding a new entry
            window.history.replaceState(0.toJsNumber(), "", window.location.href)
            currentHistoryIndexWasm = 0
        } else {
            // Try to extract the index from state
            currentHistoryIndexWasm = try {
                (st as? Double)?.toInt() ?: 0
            } catch (_: Throwable) {
                0
            }
        }
    } catch (_: Throwable) {
        currentHistoryIndexWasm = 0
    }

    // popstate handler: detect direction; block forward, allow back
    val onPop: (Event) -> Unit = { _ ->
        try {
            val st = window.history.state
            val newIndex = try {
                (st as? Double)?.toInt() ?: 0
            } catch (_: Throwable) {
                0
            }
            when {
                newIndex > currentHistoryIndexWasm -> {
                    // User is trying to go forward: block by going back immediately
                    window.history.go(-1)
                }
                newIndex < currentHistoryIndexWasm -> {
                    // Back navigation
                    currentHistoryIndexWasm = newIndex
                    navController.popBackStack()
                }
                else -> {
                    // Same index (e.g., replaceState) – ignore
                }
            }
        } catch (_: Throwable) {
        }
    }

    // keydown handler: Backspace, Alt+ArrowLeft, Escape; also block forward shortcuts
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
        // Block forward shortcuts on web
        if ((ke.altKey && key == "ArrowRight") || key == "BrowserForward") {
            try {
                ke.preventDefault()
            } catch (_: Throwable) { }
        }
    }

    // listener to push history entries on navigation so browser back works
    val navListener: (String) -> Unit = { route ->
        try {
            // Increment our index and push new history state with hash route
            currentHistoryIndexWasm += 1
            window.history.pushState(currentHistoryIndexWasm.toJsNumber(), "", "#${route}")
        } catch (_: Throwable) {
        }
    }

    window.addEventListener("popstate", onPop)
    window.addEventListener("keydown", onKeyDown)
    navController.addNavigateListener(navListener)

    wasmCleanup = {
        try {
            window.removeEventListener("popstate", onPop)
            window.removeEventListener("keydown", onKeyDown)
            navController.removeNavigateListener(navListener)
        } catch (_: Throwable) {
        }
    }
}

actual fun getBackHandlerCleanup(navController: SimpleNavController): () -> Unit {
    return wasmCleanup ?: { }
}
