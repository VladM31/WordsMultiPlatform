package vm.words.ua.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.main.ui.hints.HomeScreenHintStep
import vm.words.ua.main.ui.hints.createHomeScreenHintController
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.Screen.WordList
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.utils.hints.ui.components.SimpleHintHost
import vm.words.ua.utils.hints.ui.utils.viewHint
import vm.words.ua.utils.net.hasInternet

@Composable
fun HomeScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val hintController = createHomeScreenHintController()
    val isOnline = hasInternet()

    val buttons = remember(isOnline, hintController.currentStep) {
        listOf(
            GridButtonItem(
                text = "Words",
                isAvailable = isOnline,
                modifier = Modifier.viewHint(
                    step = HomeScreenHintStep.WORDS_BUTTON,
                    current = hintController.currentStep
                )
            ) { navController.navigate(WordList) },
            GridButtonItem(
                text = "My Words",
                modifier = Modifier.viewHint(
                    step = HomeScreenHintStep.MY_WORDS_BUTTON,
                    current = hintController.currentStep
                )
            ) { navController.navigate(Screen.UserWords) },
            GridButtonItem(
                text = "Add Word",
                isAvailable = isOnline,
                modifier = Modifier.viewHint(
                    step = HomeScreenHintStep.ADD_WORD_BUTTON,
                    current = hintController.currentStep
                )
            ) { navController.navigate(Screen.DefaultAddWord) },

            GridButtonItem(
                text = "Instruction",
                isAvailable = isOnline,
                modifier = Modifier.viewHint(
                    step = HomeScreenHintStep.INSTRUCTION_BUTTON,
                    current = hintController.currentStep
                )
            ) {
                navController.navigate(Screen.Instruction)
            },
            GridButtonItem(
                text = "Explore Playlists",
                isAvailable = isOnline,
                modifier = Modifier.viewHint(
                    step = HomeScreenHintStep.EXPLORE_PLAYLISTS_BUTTON,
                    current = hintController.currentStep
                )
            ) { navController.navigate(Screen.ExplorePlayLists) },

        )
    }

    SimpleHintHost(
        onNext = hintController.doNext,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppTheme.PrimaryBack)
        ) {
            AppToolBar(title = "", showBackButton = false)

            ButtonsGrid(
                items = buttons,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            BottomNavBar(
                currentRoute = Screen.Home,
                playListHintStep = HomeScreenHintStep.PLAY_LIST_BUTTON,
                homeHintStep = HomeScreenHintStep.HOME_BUTTON,
                settingHintStep = HomeScreenHintStep.SETTINGS_BUTTON,
                currentHintStep = hintController.currentStep,
            ) { route ->
                navController.navigateAndClear(route)
            }
        }
    }
}
