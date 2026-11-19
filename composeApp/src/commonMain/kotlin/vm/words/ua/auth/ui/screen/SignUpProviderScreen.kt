package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.managers.GoogleAuthManager
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.core.ui.icons.GoogleIcon
import vm.words.ua.core.ui.icons.TelegramIcon
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun SignUpProviderScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val googleApiManager = rememberInstance<GoogleApiManager>()
    val googleAuthManager = rememberInstance<GoogleAuthManager>()
    val telegramWebAppManager = rememberInstance<TelegramWebAppManager>()
    val isGoogleAvailable = remember { googleApiManager.isAvailable() && telegramWebAppManager.isAvailable.not() }
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf<ErrorMessage?>(null) }

    val buttons = remember {
        listOf(
            GridButtonItem(text = "Telegram", icon = TelegramIcon) {
                // Регистрации отдельно нет: аккаунт создаётся при подтверждении входа в боте
                navController.navigate(Screen.TelegramSignUp)
            },
            GridButtonItem(text = "Google", icon = GoogleIcon, isAvailable = isGoogleAvailable) {
                // Вход и регистрация через Google это один вызов: POST /auth/google
                scope.launch {
                    val result = googleAuthManager.login()
                    if (result.success) {
                        navController.navigateAndClear(Screen.Home)
                        return@launch
                    }
                    error = ErrorMessage(message = result.message ?: "Google sign-in failed")
                }
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

        error?.let {
            ErrorMessageBox(message = it, onDismiss = { error = null })
        }
    }
}
