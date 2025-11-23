package vm.words.ua.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.Screen.WordList
import vm.words.ua.navigation.SimpleNavController

@Composable
fun HomeScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {

    val buttons = listOf(
        GridButtonItem("Words") { navController.navigate(WordList) },
        GridButtonItem("My Words") { navController.navigate(Screen.UserWords) },
        GridButtonItem("Add Word") { navController.navigate(Screen.DefaultAddWord) },
        GridButtonItem("Instruction") {
            navController.navigate(Screen.Instruction)
        }

    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(title = "Menu", showBackButton = false)

        ButtonsGrid(
            items = buttons,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        BottomNavBar(
            currentRoute = Screen.Home,
            onNavigate = { route -> navController.navigateAndClear(route) })
    }
}
