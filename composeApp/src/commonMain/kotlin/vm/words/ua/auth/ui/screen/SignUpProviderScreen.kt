package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun SignUpProviderScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {


    val buttons = remember {
        listOf(
            GridButtonItem(text = "Telegram") {
                navController.navigate(Screen.TelegramSignUp)
            },
            GridButtonItem(text = "Google") {
                navController.navigate(Screen.GoogleSignUp)
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(title = "Providers", showBackButton = false)

        ButtonsGrid(
            items = buttons,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

    }
}