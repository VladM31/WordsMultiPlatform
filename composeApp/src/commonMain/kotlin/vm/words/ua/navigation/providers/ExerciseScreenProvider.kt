package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
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
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider

class ExerciseScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: vm.words.ua.navigation.SimpleNavController
    ): Boolean {
        when (route) {
            Screen.ExerciseSelection.route -> {
                ExerciseSelectionScreen(navController = navController)
            }

            Screen.WriteByImageAndTranslation.route -> {
                WriteByImageAndTranslationScreen(navController = navController)
            }

            Screen.WriteByImageAndDescription.route -> {
                WriteByImageAndDescriptionScreen(navController = navController)
            }

            Screen.LetterMatchByTranslation.route -> {
                LetterMatchByTranslationScreen(navController = navController)
            }

            Screen.LetterMatchByDescription.route -> {
                LetterMatchByDescriptionScreen(navController = navController)
            }

            Screen.OptionDescriptionByWords.route -> {
                DescriptionByWordsScreen(navController = navController)
            }

            Screen.OptionWordByDescription.route -> {
                WordByDescriptionsScreen(navController = navController)
            }

            Screen.OptionWordByOriginal.route -> {
                WordByOriginalsScreen(navController = navController)
            }

            Screen.OptionWordByTranslate.route -> {
                WordByTranslatesScreen(navController = navController)
            }

            Screen.MatchWords.route -> {
                MatchWordsScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}

