package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.auth.ui.actions.TelegramLoginAction
import vm.words.ua.auth.ui.vms.TelegramLoginVm
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.PrimaryButton
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

    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigate(Screen.Home)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AppToolBar(title = "Telegram login", onBackClick = { navController.popBackStack() })

        Spacer(modifier = Modifier.height(12.dp))

        // Input or status block
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.code.isNotBlank()) {
                // show message that user should open telegram and confirm
                Text(
                    text = "Please open Telegram and confirm login",
                    color = AppTheme.PrimaryGreen,
                    fontSize = rememberFontSize() * 1.3f,
                    lineHeight = rememberFontSize() * 1.4f,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                AppTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.sent(TelegramLoginAction.SetPhoneNumber(it)) },
                    label = "Phone number",
                    modifier = Modifier.fillMaxWidth(),
                    helperText = "Include country code, e.g., 11234567890, 3801234567890. If you didn't sign up, please do it first."
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Buttons / Progress
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = AppTheme.PrimaryGreen)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (state.code.isBlank()) {
                PrimaryButton(
                    text = "Submit",
                    onClick = { viewModel.sent(TelegramLoginAction.Submit) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                PrimaryButton(
                    text = "Open Telegram",
                    onClick = {
                        uriHandler.openUri(AppRemoteConfig.telegramBotLink)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }

    state.errorMessage?.let {
        ErrorMessageBox(it)
    }
}
