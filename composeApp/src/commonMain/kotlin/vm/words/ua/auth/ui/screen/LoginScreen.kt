package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.auth.ui.components.LoginForm
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LoginScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    onLogin: (phone: String, password: String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Login",
            showBackButton = false
        )

        CenteredContainer(maxWidth = 300.dp) {
            VerticalCenteredContainer(
                modifier = Modifier.padding(16.dp)
            ) {
                LoginForm(
                    onLogin = onLogin,
                    onJoinNowClick = { navController.navigate("signup") },
                    onTelegramClick = { navController.navigate("telegram_login") }
                )
            }
        }
    }
}
