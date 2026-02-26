package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.auth.ui.actions.TelegramLoginAction
import vm.words.ua.auth.ui.states.TelegramLoginState
import vm.words.ua.auth.ui.vms.TelegramLoginVm
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.ui.states.ToastData
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun TelegramLoginScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: TelegramLoginVm = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    val toaster = rememberToast()
    val toastData = rememberToastData(navController)

    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigate(Screen.Home)
        }
    }

    LaunchedEffect(state.isNotFoundId) {
        state.isNotFoundId?.let {
            toaster.show(toastData)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolBar(title = "Telegram login", onBackClick = { navController.popBackStack() })

        Column(
            modifier = modifier
                .fillMaxHeight()
                .widthIn(max = 480.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
                Spacer(modifier = Modifier.height(12.dp))
                return@Column
            }



            // Input or status block
            InputBox(state, viewModel)

            Spacer(modifier = Modifier.weight(1f))

            // Buttons / Progress
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (state.code.isNotBlank()) {
                    PrimaryButton(
                        text = "Open Telegram",
                        onClick = {
                            uriHandler.openUri(AppRemoteConfig.telegramBotLink)
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    return@Column
                }
                PrimaryButton(
                    text = "Submit",
                    onClick = { viewModel.sent(TelegramLoginAction.Submit) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                if (state.isMiniApp.not()) {
                    return@Column
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Or sing in with current Telegram session in the app",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize() * 1.1f,
                    lineHeight = rememberFontSize() * 1.3f,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = "Sign in with Telegram",
                    onClick = { viewModel.sent(TelegramLoginAction.SubmitMiniApp) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

            }
        }
    }


    state.errorMessage?.let {
        ErrorMessageBox(it)
    }
    AppToast(toaster)
}

@Composable
private fun rememberToastData(navController: SimpleNavController): ToastData = remember {
    ToastData(
        message = "Your didn't sign up with Telegram yet",
        buttonText = "Sign up",
        onButtonClick = {
            navController.navigateAndClearCurrent(Screen.TelegramSignUp)
        },
        duration = ToastData.Duration.Long
    )
}

@Composable
private fun InputBox(
    state: TelegramLoginState,
    viewModel: TelegramLoginVm
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.code.isNotBlank()) {
            Text(
                text = "Please open Telegram and confirm login",
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize() * 1.3f,
                lineHeight = rememberFontSize() * 1.4f,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            return
        }
        AppTextField(
            value = state.phoneNumber,
            onValueChange = { viewModel.sent(TelegramLoginAction.SetPhoneNumber(it)) },
            label = "Phone number",
            modifier = Modifier.fillMaxWidth(),
            helperText = "Include country code, e.g., 11234567890, 3801234567890. If you didn't sign up, please do it first."
        )
    }
}
