package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class SimpleNavController {
    var currentRoute by mutableStateOf<String>("loader")
        private set

    private val backStack = mutableListOf<String>()
    private val navigateListeners = mutableListOf<(String) -> Unit>()
    private val navigateParams = mutableMapOf<String, Any?>()
    private val returnParams = mutableMapOf<String, Any?>()

    fun navigate(screen: Screen) {
        this.navigate(screen.route)
    }

    fun navigate(screen: Screen, param: Any?) {
        this.navigate(screen.route, param)
    }

    fun navigate(route: String) {
        navigate(route, null)
    }

    fun navigate(route: String, param: Any?) {
        backStack.add(currentRoute)
        currentRoute = route
        navigateParams[route] = param
        // Clear return params when navigating to new screen
        returnParams.remove(currentRoute)
        navigateListeners.forEach { it(route) }
    }

    fun navigateAndClear(route: String) {
        backStack.clear()
        navigateParams.clear()
        returnParams.clear()
        currentRoute = route
        navigateListeners.forEach { it(route) }
    }

    fun navigateAndClear(screen: Screen) {
        this.navigateAndClear(screen.route)
    }

    fun popBackStack(returnParam: Any? = null): Boolean {
        return if (backStack.isNotEmpty()) {
            val previousRoute = backStack.last()
            // Store return parameter for previous screen
            if (returnParam != null) {
                returnParams[previousRoute] = returnParam
            }
            currentRoute = backStack.removeAt(backStack.lastIndex)
            true
        } else {
            false
        }
    }

    fun popBackStackTo(route: String, inclusive: Boolean = false, returnParam: Any? = null): Boolean {
        val index = backStack.lastIndexOf(route)
        if (index == -1) return false

        val targetIndexExclusive = if (inclusive) index - 1 else index

        while (backStack.size - 1 > targetIndexExclusive) {
            backStack.removeAt(backStack.lastIndex)
        }

        return if (backStack.isNotEmpty()) {
            val previousRoute = backStack.last()
            // Store return parameter for target screen
            if (returnParam != null) {
                returnParams[previousRoute] = returnParam
            }
            currentRoute = backStack.removeAt(backStack.lastIndex)
            true
        } else {
            currentRoute = "loader"
            backStack.clear()
            true
        }
    }

    fun popBackStackTo(screen: Screen, inclusive: Boolean = false, returnParam: Any? = null): Boolean {
        return this.popBackStackTo(screen.route, inclusive, returnParam)
    }

    /**
     * Get parameter passed to current screen via navigate()
     */
    fun <T> getParam(route: String = currentRoute): T? {
        @Suppress("UNCHECKED_CAST")
        return navigateParams[route] as? T
    }

    /**
     * Get parameter returned from child screen via popBackStack()
     */
    fun <T> getReturnParam(route: String = currentRoute): T? {
        @Suppress("UNCHECKED_CAST")
        val param = returnParams[route] as? T
        // Clear after reading to prevent stale data
        returnParams.remove(route)
        return param
    }

    /**
     * Clear parameter for specific route
     */
    fun clearParam(route: String = currentRoute) {
        navigateParams.remove(route)
        returnParams.remove(route)
    }

    fun addNavigateListener(listener: (String) -> Unit) {
        navigateListeners.add(listener)
    }

    fun removeNavigateListener(listener: (String) -> Unit) {
        navigateListeners.remove(listener)
    }

    /**
     * Restore navigation state
     */
    internal fun restoreState(route: String, stack: List<String>) {
        currentRoute = route
        backStack.clear()
        backStack.addAll(stack)
    }

    /**
     * Get current navigation state for saving
     */
    internal fun getState(): Pair<String, List<String>> {
        return currentRoute to backStack.toList()
    }
}

@Composable
fun rememberSimpleNavController(): SimpleNavController {
    return rememberSaveable(
        saver = Saver(
            save = { controller ->
                val (route, stack) = controller.getState()
                // Save as list: [currentRoute, ...backStack]
                listOf(route) + stack
            },
            restore = { savedList ->
                val controller = SimpleNavController()
                if (savedList.isNotEmpty()) {
                    val currentRoute = savedList[0]
                    val backStack = savedList.drop(1)
                    controller.restoreState(currentRoute, backStack)
                }
                controller
            }
        )
    ) {
        SimpleNavController()
    }
}
