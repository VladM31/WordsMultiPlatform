package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.main.ui.screen.HomeScreen
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider

class MainScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: vm.words.ua.navigation.SimpleNavController
    ): Boolean {
        when (route) {
            Screen.Home.route -> {
                HomeScreen(navController = navController)
            }
            else -> {
                return false
            }
        }
        return true
    }
}

