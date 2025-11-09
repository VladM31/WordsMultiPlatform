package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.core.ui.screen.UpdateScreen
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.navigation.SimpleNavController

class CoreScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: SimpleNavController
    ): Boolean {
        when (route) {
            Screen.UpdateApp.route -> {
                UpdateScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}