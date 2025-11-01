package vm.words.ua.navigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.kodein.di.DI
import org.kodein.di.instance
import vm.words.ua.auth.ui.screen.ConfirmSignUpScreen
import vm.words.ua.auth.ui.screen.LoginScreen
import vm.words.ua.auth.ui.screen.SignUpScreen
import vm.words.ua.auth.ui.screen.TelegramLoginScreen
import vm.words.ua.di.DiContainer
import vm.words.ua.exercise.ui.screens.*
import vm.words.ua.main.ui.screen.HomeScreen
import vm.words.ua.playlist.ui.screen.PlayListDetailsScreen
import vm.words.ua.playlist.ui.screen.PlayListFilterScreen
import vm.words.ua.playlist.ui.screen.PlayListScreen
import vm.words.ua.settings.ui.screen.SettingScreen
import vm.words.ua.words.ui.screen.WordDetailsScreen

@Composable
fun AppNavGraph() {
    val navController: SimpleNavController by DiContainer.di.instance()
    when (navController.currentRoute) {
        Screen.Login.route -> {
            LoginScreen(navController = navController)
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
            PlayListDetailsScreen(
                navController = navController
            )
        }

        Screen.WordDetails.route -> {
            WordDetailsScreen(
                navController = navController
            )
        }

        Screen.ExerciseSelection.route -> {
            ExerciseSelectionScreen(
                navController = navController
            )
        }

        Screen.WriteByImageAndTranslation.route,
        Screen.WriteByImageAndDescription.route,
        Screen.MatchWords.route,
        Screen.LetterMatchByTranslation.route,
        Screen.LetterMatchByDescription.route -> {
            Button(onClick = { navController.popBackStack() }) {
                Text("Exercise Screen, " + navController.currentRoute)
            }
        }
        Screen.OptionDescriptionByWords.route -> {
            DescriptionByWordsScreen(
                navController = navController
            )
        }
        Screen.OptionWordByDescription.route -> {
            WordByDescriptionsScreen(
                navController = navController
            )
        }
        Screen.OptionWordByOriginal.route -> {
            WordByOriginalsScreen(
                navController = navController
            )
        }
        Screen.OptionWordByTranslate.route -> {
            WordByTranslatesScreen(
                navController = navController
            )
        }
        else -> {
            Button(onClick = { navController.popBackStack() }) {
                Text("Unknown Screen, " + navController.currentRoute)
            }
        }
    }
}
