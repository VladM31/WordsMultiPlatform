package vm.words.ua.navigation

import androidx.compose.runtime.compositionLocalOf

/**
 * Provides the route of the screen currently being composed inside NavTransition.
 * Unlike SimpleNavController.currentRoute (which updates immediately on navigation),
 * this always holds the route that belongs to the composable currently in the tree —
 * including during AnimatedContent exit animations.
 */
val LocalCurrentRoute = compositionLocalOf { "loader" }

