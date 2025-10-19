package vm.words.ua.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.main.ui.components.BottomNavBar
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem

@Composable
fun HomeScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val textSize = 40.sp

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
                .weight(1f),
            maxButtonWidth = 300.dp,
            textSizeSp = textSize.value
        )

        BottomNavBar(currentRoute = "home", onNavigate = { route -> navController.navigate(route) })
    }
}
