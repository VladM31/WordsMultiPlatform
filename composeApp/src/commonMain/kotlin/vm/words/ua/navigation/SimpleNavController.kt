package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class SimpleNavController {
    var currentRoute by mutableStateOf<String>("loader")
        private set

    private val backStack = mutableListOf<String>()

    // listeners for navigation events (used by platform integrations)
    private val navigateListeners = mutableListOf<(String) -> Unit>()

    fun navigate(route: String) {
        backStack.add(currentRoute)
        currentRoute = route
        // notify listeners
        navigateListeners.forEach { it(route) }
    }

    fun navigateAndClear(route: String) {
        backStack.clear()
        currentRoute = route
        navigateListeners.forEach { it(route) }
    }

    fun popBackStack(): Boolean {
        return if (backStack.isNotEmpty()) {
            currentRoute = backStack.removeLast()
            true
        } else {
            false
        }
    }

    /**
     * Pop the back stack up to the last occurrence of [route].
     * If [inclusive] == false: the controller will pop entries above [route] and then set currentRoute = [route].
     * If [inclusive] == true: the controller will also remove [route] itself and set currentRoute to the route below it.
     * Returns true if the operation succeeded (the [route] was present), false otherwise.
     *
     * Note: if [inclusive] == true and [route] is the bottom-most element (no route below it),
     * the currentRoute will be set to "loader" (the initial default) and the back stack cleared.
     */
    fun popBackStackTo(route: String, inclusive: Boolean = false): Boolean {
        val index = backStack.lastIndexOf(route)
        if (index == -1) return false

        // targetIndexExclusive is the index of the element that should remain above (not removed)
        // after trimming the stack. For inclusive == false we want to keep route and then remove it
        // from backStack when assigning currentRoute; for inclusive == true we want to remove route
        // as well and land on the previous element.
        val targetIndexExclusive = if (inclusive) index - 1 else index

        // Remove elements from the end until the last index equals targetIndexExclusive
        while (backStack.size - 1 > targetIndexExclusive) {
            backStack.removeLast()
        }

        // After trimming, if there is at least one element, pop it and set as currentRoute.
        return if (backStack.isNotEmpty()) {
            currentRoute = backStack.removeLast()
            true
        } else {
            // No element left to pop to: set a sensible default (loader) and return true
            currentRoute = "loader"
            backStack.clear()
            true
        }
    }

    fun addNavigateListener(listener: (String) -> Unit) {
        navigateListeners.add(listener)
    }

    fun removeNavigateListener(listener: (String) -> Unit) {
        navigateListeners.remove(listener)
    }

    fun navigate(screen: Screen) {
        this.navigate(screen.route)
    }

    fun navigateAndClear(screen: Screen) {
        this.navigateAndClear(screen.route)
    }

    fun popBackStackTo(screen: Screen, inclusive: Boolean = false): Boolean{
        return this.popBackStackTo(screen.route, inclusive)
    }
}

@Composable
fun rememberSimpleNavController(): SimpleNavController {
    return remember { SimpleNavController() }
}
