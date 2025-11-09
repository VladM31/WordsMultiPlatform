package vm.words.ua.navigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.kodein.di.instance
import vm.words.ua.auth.ui.screen.ConfirmSignUpScreen
import vm.words.ua.auth.ui.screen.LoginScreen
import vm.words.ua.auth.ui.screen.SignUpScreen
import vm.words.ua.auth.ui.screen.TelegramLoginScreen
import vm.words.ua.core.ui.screen.UpdateScreen
import vm.words.ua.di.DiContainer
import vm.words.ua.exercise.ui.screens.DescriptionByWordsScreen
import vm.words.ua.exercise.ui.screens.ExerciseSelectionScreen
import vm.words.ua.exercise.ui.screens.LetterMatchByDescriptionScreen
import vm.words.ua.exercise.ui.screens.LetterMatchByTranslationScreen
import vm.words.ua.exercise.ui.screens.MatchWordsScreen
import vm.words.ua.exercise.ui.screens.WordByDescriptionsScreen
import vm.words.ua.exercise.ui.screens.WordByOriginalsScreen
import vm.words.ua.exercise.ui.screens.WordByTranslatesScreen
import vm.words.ua.exercise.ui.screens.WriteByImageAndDescriptionScreen
import vm.words.ua.exercise.ui.screens.WriteByImageAndTranslationScreen
import vm.words.ua.main.ui.screen.HomeScreen
import vm.words.ua.playlist.ui.screen.PlayListDetailsScreen
import vm.words.ua.playlist.ui.screen.PlayListFilterScreen
import vm.words.ua.playlist.ui.screen.PlayListScreen
import vm.words.ua.settings.ui.screen.SettingScreen
import vm.words.ua.words.ui.screen.UserWordFilterScreen
import vm.words.ua.words.ui.screen.UserWordsScreen
import vm.words.ua.words.ui.screen.WordDetailsScreen
import vm.words.ua.words.ui.screen.WordFilterScreen
import vm.words.ua.words.ui.screen.WordsScreen

@Composable
fun AppNavGraph() {
    val navController: SimpleNavController by DiContainer.di.instance()
    when (navController.currentRoute) {
        Screen.UpdateApp.route -> {
            UpdateScreen(navController = navController)
        }

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

        Screen.WordFilter.route -> {
            WordFilterScreen(
                navController = navController
            )
        }

        Screen.WordDetails.route -> {
            WordDetailsScreen(
                navController = navController
            )
        }

        Screen.WordList.route -> {
            WordsScreen(
                navController = navController
            )
        }

        Screen.UserWordsFilter.route -> {
            UserWordFilterScreen(
                navController = navController
            )
        }

        Screen.UserWords.route -> {
            UserWordsScreen(
                navController = navController
            )
        }

        Screen.ExerciseSelection.route -> {
            ExerciseSelectionScreen(
                navController = navController
            )
        }

        Screen.WriteByImageAndTranslation.route -> {
            WriteByImageAndTranslationScreen(
                navController = navController
            )
        }

        Screen.WriteByImageAndDescription.route -> {
            WriteByImageAndDescriptionScreen(
                navController = navController
            )
        }

        Screen.LetterMatchByTranslation.route -> {
            LetterMatchByTranslationScreen(
                navController = navController
            )
        }
        Screen.LetterMatchByDescription.route -> {
            LetterMatchByDescriptionScreen(
                navController = navController
            )
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
        Screen.MatchWords.route -> {
            MatchWordsScreen(
                navController = navController
            )
        }
        else -> {
            Button(onClick = { navController.popBackStack() }) {
                Text("Unknown Screen, " + navController.currentRoute)
            }
        }
    }
    BackHandler(navController = navController)
}
