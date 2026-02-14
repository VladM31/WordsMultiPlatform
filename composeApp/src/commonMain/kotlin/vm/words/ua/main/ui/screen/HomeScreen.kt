package vm.words.ua.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.core.utils.rememberWidthDeviceFormat
import vm.words.ua.main.ui.hints.HomeScreenHintController
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
    val isPhoneFormat = rememberWidthDeviceFormat().isPhone
    val spacerHeight = remember(isPhoneFormat) {
        if (isPhoneFormat) 8.dp else 96.dp
    }

    val buttons = rememberButtons(isOnline, hintController, navController)

    SimpleHintHost(
        onNext = hintController.doNext,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppTheme.PrimaryBack)
        ) {
            Spacer(modifier = Modifier.height(spacerHeight))

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

@Composable
private fun rememberButtons(
    isOnline: Boolean,
    hintController: HomeScreenHintController,
    navController: SimpleNavController
): List<GridButtonItem> = remember(isOnline, hintController.currentStep) {
    listOf(
        GridButtonItem(
            text = "Words",
            isAvailable = isOnline,
            icon = Icons.AutoMirrored.Filled.MenuBook,
            modifier = Modifier.viewHint(
                step = HomeScreenHintStep.WORDS_BUTTON,
                current = hintController.currentStep
            )
        ) { navController.navigate(WordList) },
        GridButtonItem(
            text = "My Words",
            icon = Icons.Outlined.Star,
            modifier = Modifier.viewHint(
                step = HomeScreenHintStep.MY_WORDS_BUTTON,
                current = hintController.currentStep
            )
        ) { navController.navigate(Screen.UserWords) },
        GridButtonItem(
            text = "Add Word",
            isAvailable = isOnline,
            icon = Icons.Filled.Add,
            modifier = Modifier.viewHint(
                step = HomeScreenHintStep.ADD_WORD_BUTTON,
                current = hintController.currentStep
            )
        ) { navController.navigate(Screen.DefaultAddWord) },

        GridButtonItem(
            text = "Instruction",
            isAvailable = isOnline,
            icon = Icons.Filled.Info,
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
            icon = Icons.Filled.Explore,
            modifier = Modifier.viewHint(
                step = HomeScreenHintStep.EXPLORE_PLAYLISTS_BUTTON,
                current = hintController.currentStep
            )
        ) { navController.navigate(Screen.ExplorePlayLists) },

        )
}
