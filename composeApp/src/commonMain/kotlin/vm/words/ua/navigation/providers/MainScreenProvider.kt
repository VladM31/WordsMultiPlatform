package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.main.ui.screen.HomeScreen
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.settings.ui.screen.SettingScreen

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

            Screen.Settings.route -> {
                SettingScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}

