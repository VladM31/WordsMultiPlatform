package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.services.GoogleSignInService
import vm.words.ua.auth.ui.components.LoginForm
import vm.words.ua.auth.ui.hints.createLoginScreenHintController
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.CenteredContainer
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.VerticalCenteredContainer
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
    val googleSignInService = rememberInstance<GoogleSignInService>()
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()
    val error = viewModel.state.map { it.errorMessage }
        .distinctUntilChanged()
        .collectAsState(initial = null)

    // State for Google Sign-In result dialog
    var googleSignInResult by remember { mutableStateOf<Pair<String, String>?>(null) }
    var googleSignInError by remember { mutableStateOf<String?>(null) }

    // Check if Google Sign-In is available
    val isGoogleSignInAvailable = remember { googleSignInService.isAvailable() }

    // Navigate when login is successful
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigateAndClear(Screen.Home)
        }
    }

    SimpleHintHost(
        onNext = hintController.doNext
    ) {
        BoxWithConstraints {
            val boxMaxWith = maxWidth
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
                        BoxWithConstraints {
                            LoginForm(
                                viewModel = viewModel,
                                maxWidth = boxMaxWith,
                                currentHintStep = hintController.currentStep,
                                onJoinNowClick = { navController.navigate(Screen.SignUp) },
                                onTelegramClick = { navController.navigate(Screen.TelegramLogin) },
                                onGoogleClick = {
                                    scope.launch {
                                        val result = googleSignInService.signIn()
                                        if (result.success) {
                                            googleSignInResult = Pair(
                                                result.email ?: "Unknown",
                                                result.userId ?: "Unknown"
                                            )
                                        } else {
                                            googleSignInError = result.errorMessage
                                        }
                                    }
                                },
                                showGoogleSignIn = isGoogleSignInAvailable
                            )
                        }

                        // Display error message if present
                        error.value?.let { errorMessage ->
                            Spacer(modifier = Modifier.height(12.dp))
                            ErrorMessageBox(message = errorMessage)
                        }
                    }
                }
            }
        }
    }

    // Google Sign-In Success Dialog
    googleSignInResult?.let { (email, userId) ->
        AlertDialog(
            onDismissRequest = { googleSignInResult = null },
            title = { Text("Google Sign-In Success") },
            text = {
                Column {
                    Text("Email: $email")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("User ID: $userId")
                }
            },
            confirmButton = {
                TextButton(onClick = { googleSignInResult = null }) {
                    Text("OK")
                }
            }
        )
    }

    // Google Sign-In Error Dialog
    googleSignInError?.let { errorMsg ->
        AlertDialog(
            onDismissRequest = { googleSignInError = null },
            title = { Text("Google Sign-In Error") },
            text = { Text(errorMsg) },
            confirmButton = {
                TextButton(onClick = { googleSignInError = null }) {
                    Text("OK")
                }
            }
        )
    }
}
