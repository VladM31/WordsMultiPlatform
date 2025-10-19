package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import vm.words.ua.auth.ui.screen.ConfirmSignUpScreen
import vm.words.ua.auth.ui.screen.LoginScreen
import vm.words.ua.auth.ui.screen.SignUpScreen
import vm.words.ua.auth.ui.screen.TelegramLoginScreen
import vm.words.ua.core.firebase.AppRemoteConfig
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.di.initDi
import vm.words.ua.main.ui.screen.HomeScreen

@Composable
fun AppNavGraph(
    navController: SimpleNavController
) {
    when (navController.currentRoute) {
        Screen.Loader.route -> {
            LoaderScreen()

            LaunchedEffect(Unit) {
                initDi()
                AppRemoteConfig.initialize()
                navController.navigateAndClear(Screen.Login)
            }
        }

        Screen.Login.route -> {
            LoginScreen(
                navController = navController
            )
        }

        Screen.SignUp.route -> {
            SignUpScreen(
                navController = navController,
                onSignUp = { phone, password, email, firstName, lastName, currency ->
                    // TODO: Implement signup logic
                }
            )
        }

        Screen.TelegramLogin.route -> {
            TelegramLoginScreen(
                navController = navController,
                onSubmit = { phoneNumber ->
                    // TODO: Implement telegram login logic
                },
                onOpenTelegram = {
                    // TODO: Open telegram app
                }
            )
        }

        Screen.ConfirmSignUp.route -> {
            ConfirmSignUpScreen(
                navController = navController,
                onOpenTelegramBot = {
                    // TODO: Open telegram bot
                }
            )
        }

        Screen.Home.route -> {
            HomeScreen(
                navController = navController
            )
        }
    }
}
