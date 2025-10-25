package vm.words.ua.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.navigation.Screen

@Composable
fun HomeScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "words" to "Words",
        "user_words" to "My Words",
        "add_word" to "Add Word",
        "instruction" to "Instruction"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(title = "Menu", showBackButton = false)

        ButtonsGrid(
            items = items.map { (route, title) -> GridButtonItem(title) { navController.navigate(route) } },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        BottomNavBar(currentRoute = Screen.Home, onNavigate = { route -> navController.navigate(route) })
    }
}
