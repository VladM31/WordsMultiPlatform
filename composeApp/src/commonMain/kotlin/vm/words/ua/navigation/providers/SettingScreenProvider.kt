package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.settings.ui.screen.PolicyScreen
import vm.words.ua.settings.ui.screen.ProfileScreen
import vm.words.ua.settings.ui.screen.SettingScreen
import vm.words.ua.settings.ui.screen.ThemeScreen

class SettingScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: SimpleNavController
    ): Boolean {
        when (route) {
            Screen.Settings.route -> {
                SettingScreen(navController = navController)
            }

            Screen.Policy.route -> {
                PolicyScreen(navController = navController)
            }

            Screen.Profile.route -> {
                ProfileScreen(navController = navController)
            }

            Screen.Theme.route -> {
                ThemeScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}