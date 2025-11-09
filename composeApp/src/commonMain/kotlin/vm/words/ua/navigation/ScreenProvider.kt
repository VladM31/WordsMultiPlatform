package vm.words.ua.navigation

import androidx.compose.runtime.Composable

/**
 * Interface for modules to provide screens for navigation.
 * Implementations should render the screen when [provide] returns true for the given route.
 */
interface ScreenProvider {
    /**
     * Attempt to render a screen for [route]. Return true if the provider handled this route.
     */
    @Composable
    fun provide(route: String, navController: SimpleNavController): Boolean
}

