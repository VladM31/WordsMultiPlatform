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

    fun navigate(route: String) {
        backStack.add(currentRoute)
        currentRoute = route
    }

    fun navigateAndClear(route: String) {
        backStack.clear()
        currentRoute = route
    }

    fun popBackStack(): Boolean {
        return if (backStack.isNotEmpty()) {
            currentRoute = backStack.removeLast()
            true
        } else {
            false
        }
    }
}

@Composable
fun rememberSimpleNavController(): SimpleNavController {
    return remember { SimpleNavController() }
}

