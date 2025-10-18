package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.instance
import vm.words.ua.auth.ui.components.LoginForm
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LoginScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    // Get ViewModel from Kodein manually
    val viewModel: LoginViewModel by DiContainer.di.instance()
    val state by viewModel.state.collectAsState()

    // Navigate when login is successful
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigate("home")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Login",
            showBackButton = false
        )

        CenteredContainer(maxWidth = 500.dp) {
            VerticalCenteredContainer(
                modifier = Modifier.padding(16.dp)
            ) {
                LoginForm(
                    viewModel = viewModel,
                    onJoinNowClick = { navController.navigate("signup") },
                    onTelegramClick = { navController.navigate("telegram_login") },
                    showTelegramButton = true
                )

                // Display error message if present
                state.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = error.message,
                        color = AppTheme.PrimaryColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
