package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.kodein.di.direct
import org.kodein.di.instance
import vm.words.ua.auth.ui.components.LoginForm
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.core.domain.managers.UserCacheManager
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
    // Keep the resolved ViewModel stable across recompositions
    val viewModel = remember {
        DiContainer.di.direct.instance<LoginViewModel>()
    }

    val state by viewModel.state.collectAsState()
    val  error = viewModel.state.map { it.errorMessage }
        .distinctUntilChanged()
        .collectAsState(initial = null)

    // If token already present and not expired - go to home
    LaunchedEffect(Unit) {
        try {
            if (state.isNotExpired) {
                navController.navigate("home")
                return@LaunchedEffect
            }
        } catch (_: Throwable) {
            // ignore - treat as no token
        }
    }

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
                error.value?.let { errorMessage ->
                    Spacer(modifier = Modifier.height(12.dp))
                    ErrorMessageBox(message = errorMessage)
                }
            }
        }
    }
}
