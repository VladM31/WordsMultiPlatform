package vm.words.ua.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.*

class SimpleNavController {
    var currentRoute by mutableStateOf("loader")
        private set

    private val backStack = mutableListOf<String>()
    private val navigateListeners = mutableListOf<(String) -> Unit>()
    private val navigateParams = mutableMapOf<String, Any?>()
    private val returnParams = mutableMapOf<String, Any?>()

    // Keep a ViewModelStore per route
    private val viewModelOwners = mutableMapOf<String, RouteViewModelStoreOwner>()

    // Scope for delayed cleanup operations
    private val cleanupScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var isLastScreen by mutableStateOf(true)
        private set

    private fun updateIsLastScreen() {
        isLastScreen = backStack.size <= 1
    }

    // Simple multiplatform ViewModelStoreOwner
    private class RouteViewModelStoreOwner : ViewModelStoreOwner {
        private val store = ViewModelStore()
        override val viewModelStore: ViewModelStore get() = store
        fun clear() = store.clear()
    }

    /** Get or create ViewModelStoreOwner for a route (defaults to current). */
    fun viewModelStoreOwner(route: String = currentRoute): ViewModelStoreOwner =
        viewModelOwners.getOrPut(route) { RouteViewModelStoreOwner() }

    private fun clearViewModelStoreOwner(route: String) {
        val owner = viewModelOwners.remove(route) ?: return
        // Clear asynchronously to avoid CancellationException during active coroutines
        cleanupScope.launch {
            delay(5000) // Small delay to let UI update first
            try {
                owner.clear()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun navigate(screen: Screen) {
        this.navigate(screen.route)
    }

    fun navigate(screen: Screen, param: Any?) {
        this.navigate(screen.route, param)
    }


    fun navigate(route: String) {
        navigate(route, null)
    }

    private fun navigate(route: String, param: Any?) {
        backStack.add(currentRoute)
        currentRoute = route
        navigateParams[route] = param
        // Clear return params when navigating to new screen
        returnParams.remove(currentRoute)
        navigateListeners.forEach { it(route) }
        updateIsLastScreen()
    }

    fun navigateAndClearCurrent(screen: Screen, param: Any? = null) {
        this.navigateAndClearCurrent(screen.route, param)
    }

    fun navigateAndClearCurrent(route: String, param: Any? = null) {
        // Clear VM store for the current route as we replace it without stacking
        val poppedRoute = currentRoute
        currentRoute = route
        navigateParams[route] = param
        // Clear return params when navigating to new screen
        returnParams.remove(currentRoute)
        navigateListeners.forEach { it(route) }
        clearViewModelStoreOwner(poppedRoute)
    }

    private fun navigateAndClear(route: String) {
        backStack.clear()
        navigateParams.clear()
        returnParams.clear()
        currentRoute = route
        navigateListeners.forEach { it(route) }
        // Clear all ViewModel stores since history is wiped
        val owners = viewModelOwners.keys.toList()
        owners.forEach { clearViewModelStoreOwner(it) }
        updateIsLastScreen()
    }

    fun navigateAndClear(screen: Screen) {
        this.navigateAndClear(screen.route)
    }

    fun popBackStack(returnParam: Any? = null): Boolean {
        return if (backStack.isNotEmpty()) {
            val poppedRoute = currentRoute
            val previousRoute = backStack.last()
            // Store return parameter for previous screen
            returnParams[previousRoute] = returnParam
            currentRoute = backStack.removeAt(backStack.lastIndex)
            // Clear ViewModel store for the popped screen
            clearViewModelStoreOwner(poppedRoute)
            updateIsLastScreen()
            true
        } else {
            false
        }
    }

    fun popBackStackTo(route: String, inclusive: Boolean = false, returnParam: Any? = null): Boolean {
        val index = backStack.lastIndexOf(route)
        if (index == -1) return false

        val targetIndexExclusive = if (inclusive) index - 1 else index

        val removedRoutes = mutableListOf<String>()
        while (backStack.size - 1 > targetIndexExclusive) {
            removedRoutes += backStack.removeAt(backStack.lastIndex)
        }

        return if (backStack.size > 1) {
            val poppedRoute = currentRoute
            val previousRoute = backStack.last()
            // Store return parameter for target screen
            if (returnParam != null) {
                returnParams[previousRoute] = returnParam
            }
            currentRoute = backStack.removeAt(backStack.lastIndex)
            // Clear VMs for removed history routes and the popped current route
            removedRoutes.forEach { clearViewModelStoreOwner(it) }
            clearViewModelStoreOwner(poppedRoute)
            updateIsLastScreen()
            true
        } else {
            val poppedRoute = currentRoute
            currentRoute = "loader"
            backStack.clear()
            // Clear all VM stores including the previously current and any removed
            removedRoutes.forEach { clearViewModelStoreOwner(it) }
            val owners = viewModelOwners.keys.toList()
            owners.forEach { clearViewModelStoreOwner(it) }
            clearViewModelStoreOwner(poppedRoute)
            updateIsLastScreen()
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
     * Get parameter passed to current screen via navigate()
     */
    fun <T> getParamOrThrow(route: String = currentRoute): T {
        return getParam<T>(route)
            ?: throw IllegalStateException("No navigation parameter for route: ${'$'}route")
    }

    /**
     * Get parameter returned from child screen via popBackStack()
     */
    inline fun <reified T> getReturnParam(route: String = currentRoute): T? {
        return getRawReturnParam(route) as? T
    }

    fun getRawReturnParam(route: String): Any? {
        return returnParams.remove(route)
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

    fun navigateAndClear(screen: Screen, until: Screen, include: Boolean = false) {
        val untilRoute = until.route
        // If until is the current route
        if (untilRoute == currentRoute) {
            if (include) {
                // Clear everything and navigate
                navigateAndClear(screen)
            } else {
                // Just regular navigation keeps current on stack
                navigate(screen)
            }
            return
        }

        // Find last occurrence of until route in back stack
        val index = backStack.lastIndexOf(untilRoute)
        if (index == -1) {
            // Fallback: until not found, behave like full clear
            navigateAndClear(screen)
            return
        }

        // Determine preserved routes
        val preservedCount = if (include) index else index + 1
        val preserved = backStack.take(preservedCount).toMutableList()

        // Routes removed (everything after preserved plus currentRoute)
        val removed = backStack.drop(preservedCount).toMutableList().apply { add(currentRoute) }

        // Clean params & ViewModelStores for removed routes
        removed.forEach { r ->
            navigateParams.remove(r)
            returnParams.remove(r)
            clearViewModelStoreOwner(r)
        }

        // Replace back stack with preserved
        backStack.clear()
        backStack.addAll(preserved)

        // Perform navigation WITHOUT pushing old current route (custom behavior)
        currentRoute = screen.route
        navigateParams[screen.route] = null
        returnParams.remove(currentRoute)
        navigateListeners.forEach { it(screen.route) }
        updateIsLastScreen()
    }
}