package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.auth.ui.screen.LoginScreen
import vm.words.ua.auth.ui.screen.SignUpProviderScreen
import vm.words.ua.auth.ui.screen.TelegramLoginScreen
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider

class AuthScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: vm.words.ua.navigation.SimpleNavController
    ): Boolean {
        when (route) {
            Screen.Login.route -> {
                LoginScreen(navController = navController)
            }

            Screen.SignUpProvider.route -> {
                SignUpProviderScreen(navController = navController)
            }

            Screen.TelegramLogin.route -> {
                TelegramLoginScreen(
                    navController = navController
                )
            }

            Screen.TelegramSignUp.route -> {
                TelegramLoginScreen(
                    navController = navController,
                    isSignUp = true
                )
            }

            else -> {
                return false
            }
        }
        return true
    }
}

