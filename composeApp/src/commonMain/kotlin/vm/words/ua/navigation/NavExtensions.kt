package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Composable-safe version of getParamOrThrow that reads the route from LocalCurrentRoute
 * instead of SimpleNavController.currentRoute, so it works correctly during
 * AnimatedContent exit animations where currentRoute has already moved on.
 */
@Composable
inline fun <reified T> SimpleNavController.rememberParamOrThrow(): T {
    val route = LocalCurrentRoute.current
    return remember(route) {
        getParam(route)
            ?: throw IllegalStateException("No navigation parameter for route: $route")
    }
}

@Composable
inline fun <reified T> SimpleNavController.rememberParam(): T? {
    val route = LocalCurrentRoute.current
    return remember(route) {
        getParam(route)
    }
}
