package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import vm.words.ua.auth.domain.models.GoogleLoginErrorMessage
import vm.words.ua.auth.ui.actions.LoginAction
import vm.words.ua.auth.ui.components.LoginForm
import vm.words.ua.auth.ui.hints.createLoginScreenHintController
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.ui.states.ToastData
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.utils.hints.ui.components.SimpleHintHost

@Composable
fun LoginScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val hintController = createLoginScreenHintController()
    val viewModel = rememberInstance<LoginViewModel>()
    val toaster = rememberToast()

    val state by viewModel.state.collectAsState()
    val error = viewModel.state.map { it.errorMessage }
        .distinctUntilChanged()
        .collectAsState(initial = null)
    val isGoogleSignInError = state.errorMessage is GoogleLoginErrorMessage


    // Navigate when login is successful
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigateAndClear(Screen.Home)
        }
    }
    LaunchedEffect(state.isNotFoundGoogle) {
        if (state.isNotFoundGoogle) {
            viewModel.sent(LoginAction.ClearNotFound)
            navController.navigate(Screen.GoogleSignUp)
        }
    }

    LaunchedEffect(isGoogleSignInError) {
        if (isGoogleSignInError.not()) {
            return@LaunchedEffect
        }
        // You didnt sign up with google before, so show hint about it
        toaster.show(
            ToastData(
                message = "Try to sign up with Google first",
                duration = ToastData.Duration.DoubleLong,
                buttonText = "Sign Up",
                onButtonClick = { navController.navigate(Screen.GoogleSignUp) }
            )
        )
    }

    SimpleHintHost(
        onNext = hintController.doNext
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                AppToolBar(
                    title = "Login",
                    showBackButton = false
                )

                CenteredContainer(
                    maxWidth = 500.dp,
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                ) {
                    LoginForm(
                        viewModel = viewModel,
                        currentHintStep = hintController.currentStep,
                        onJoinNowClick = { navController.navigate(Screen.SignUpProvider) },
                        onTelegramClick = { navController.navigate(Screen.TelegramLogin) },
                        onGoogleClick = {
                            viewModel.sent(LoginAction.GoogleSignIn)
                        },
                        showGoogleSignIn = state.isGoogleSignInAvailable
                    )

                    // Display error message if present
                    error.value?.let { errorMessage ->
                        ErrorMessageBox(message = errorMessage, onDismiss = {
                            viewModel.sent(LoginAction.DismissErrorMessage)
                        })
                    }
                }
            }

            IconButton(
                onClick = { navController.navigate(Screen.Theme) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp)
                    .size((32 * rememberScaleFactor()).dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Change Theme",
                    tint = AppTheme.PrimaryColor.copy(alpha = 0.6f),
                    modifier = Modifier.size((20 * rememberScaleFactor()).dp)
                )
            }
        }

    }

    AppToast(toaster)
}
