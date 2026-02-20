package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.ui.screen.*

class PlayListScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: SimpleNavController
    ): Boolean {
        when (route) {
            Screen.PlayList.route -> {
                PlayListScreen(navController = navController)
            }

            Screen.PlayListFilter.route -> {
                PlayListFilterScreen(navController = navController)
            }

            Screen.PlayListDetails.route -> {
                PlayListDetailsScreen(navController = navController)
            }

            Screen.ExplorePlayLists.route -> {
                ExplorePlayListsScreen(navController = navController)
            }

            Screen.ExplorePlayListsFilter.route -> {
                ExplorePlayListsFilterScreen(navController = navController)
            }

            Screen.PublicPlayListDetails.route -> {
                PublicPlayListDetailsScreen(navController = navController)
            }

            Screen.FastStart.route -> {
                FastStartPlayListScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}