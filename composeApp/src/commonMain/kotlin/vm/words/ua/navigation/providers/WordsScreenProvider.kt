package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.words.ui.screen.*

class WordsScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: vm.words.ua.navigation.SimpleNavController
    ): Boolean {
        when (route) {
            Screen.WordList.route -> {
                WordsScreen(navController = navController)
            }

            Screen.WordDetails.route -> {
                WordDetailsScreen(navController = navController)
            }

            Screen.WordFilter.route -> {
                WordFilterScreen(navController = navController)
            }

            Screen.UserWords.route -> {
                UserWordsScreen(navController = navController)
            }

            Screen.UserWordsFilter.route -> {
                UserWordFilterScreen(navController = navController)
            }

            Screen.PinUserWords.route -> {
                PinUserWordsScreen(navController = navController)
            }

            Screen.DefaultAddWord.route -> {
                DefaultAddWordScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}

