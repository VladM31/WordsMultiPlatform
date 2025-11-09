package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.words.ui.screen.UserWordFilterScreen
import vm.words.ua.words.ui.screen.UserWordsScreen
import vm.words.ua.words.ui.screen.WordDetailsScreen
import vm.words.ua.words.ui.screen.WordFilterScreen
import vm.words.ua.words.ui.screen.WordsScreen

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

            else -> {
                return false
            }
        }
        return true
    }
}

