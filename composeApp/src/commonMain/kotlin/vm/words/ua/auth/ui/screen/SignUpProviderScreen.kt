package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.core.ui.icons.TelegramIcon
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.google_icon

@Composable
fun SignUpProviderScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val googleApiManager = rememberInstance<GoogleApiManager>()
    val isGoogleAvailable = remember { googleApiManager.isAvailable() }
    val googleIcon = painterResource(Res.drawable.google_icon)

    val buttons = remember {
        listOf(
            GridButtonItem(text = "Telegram", icon = TelegramIcon) {
                navController.navigate(Screen.TelegramSignUp)
            },
            GridButtonItem(text = "Google", iconPainter = googleIcon, isAvailable = isGoogleAvailable) {
                navController.navigate(Screen.GoogleSignUp)
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(title = "Providers", showBackButton = true, onBackClick = {
            navController.popBackStack()
        })

        ButtonsGrid(
            items = buttons,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

    }
}