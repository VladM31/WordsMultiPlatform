package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import vm.words.ua.core.ui.components.AuthWatcher
import vm.words.ua.auth.ui.screen.ConfirmSignUpScreen
import vm.words.ua.auth.ui.screen.LoginScreen
import vm.words.ua.auth.ui.screen.SignUpScreen
import vm.words.ua.auth.ui.screen.TelegramLoginScreen
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.exercise.ui.screens.ExerciseSelectionScreen
import vm.words.ua.main.ui.screen.HomeScreen
import vm.words.ua.playlist.domain.models.bundles.PlayListDetailsBundle
import vm.words.ua.playlist.ui.screen.PlayListFilterScreen
import vm.words.ua.playlist.ui.screen.PlayListScreen
import vm.words.ua.playlist.ui.screen.PlayListDetailsScreen
import vm.words.ua.settings.ui.screen.SettingScreen
import vm.words.ua.words.ui.bundles.WordDetailsBundle
import vm.words.ua.words.ui.screen.WordDetailsScreen

@Composable
fun AppNavGraph(
    navController: SimpleNavController
) {
    when (navController.currentRoute) {
        Screen.Loader.route -> {
            LoaderScreen(navController = navController)
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
        Screen.Settings.route -> {
            SettingScreen(
                navController = navController
            )
        }

        Screen.PlayList.route -> {
            PlayListScreen(
                navController = navController
            )
        }

        Screen.PlayListFilter.route -> {
            PlayListFilterScreen(
                navController = navController
            )
        }

        Screen.PlayListDetails.route -> {
            val playListId = navController.getParam<PlayListDetailsBundle>(Screen.PlayListDetails.route)?.playListId
            if (playListId == null){
                navController.popBackStack()
                return
            }

            PlayListDetailsScreen(
                playListId = playListId,
                navController = navController
            )
        }

        Screen.WordDetails.route -> {
            val bundle = navController.getParam<WordDetailsBundle>(Screen.WordDetails.route) ?: throw IllegalStateException()
            WordDetailsScreen(
                userWord = bundle.userWord,
                word = bundle.word,
                navController = navController
            )
        }

        Screen.ExerciseSelection.route -> {
            ExerciseSelectionScreen(
                navController = navController
            )
        }
    }
    AuthWatcher(navController = navController)
}
