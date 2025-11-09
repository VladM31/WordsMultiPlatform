package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.auth.ui.actions.TelegramLoginAction
import vm.words.ua.auth.ui.vms.TelegramLoginVm
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.core.utils.getFontSize
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
                    fontSize = getFontSize() * 1.3f,
                    lineHeight = getFontSize() * 1.4f,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                AppTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.sent(TelegramLoginAction.SetPhoneNumber(it)) },
                    label = "Phone number",
                    modifier = Modifier.fillMaxWidth()
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
                        // open telegram bot handled externally; just navigate to Home as fallback
                        navController.navigateAndClear(Screen.Home)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }

    // Side effects: show error and navigate when finished
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            // TODO: show a toast/snackbar - using simple snackbar host would be better
        }
    }

    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigate(Screen.Home)
        }
    }
}
