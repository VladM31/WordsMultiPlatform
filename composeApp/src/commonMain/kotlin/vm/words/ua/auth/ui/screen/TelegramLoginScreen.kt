package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import vm.words.ua.auth.ui.actions.TelegramLoginAction
import vm.words.ua.auth.ui.states.TelegramLoginState
import vm.words.ua.auth.ui.vms.TelegramLoginVm
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.ui.icons.TelegramIcon
import vm.words.ua.core.ui.states.ToastData
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun TelegramLoginScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,

    isSignUp: Boolean = false,
    viewModel: TelegramLoginVm = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    val toaster = rememberToast()
    val manager: TelegramWebAppManager = rememberInstance()
    val toastData = rememberToastData(manager, uriHandler)

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
        AppToolBar(
            title = if (isSignUp) "Telegram sign up" else "Telegram login",
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = true,
            additionalButtonVector = TelegramIcon,
            onAdditionalClick = {
                val opened = manager.openLink(AppRemoteConfig.telegramBotLink)
                if (!opened) uriHandler.openUri(AppRemoteConfig.telegramBotLink)
            }
        )

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

            InputBox(state, viewModel, isSignUp)

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons / Progress
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.code.isNotBlank()) {
                    PrimaryButton(
                        text = "Open Telegram",
                        onClick = {
                            val opened = manager.openLink(AppRemoteConfig.telegramBotLink)
                            if (!opened) uriHandler.openUri(AppRemoteConfig.telegramBotLink)
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    // Код не пришёл или номер указан неверно: сбросить ожидание и ввести номер заново
                    TextButton(onClick = { viewModel.sent(TelegramLoginAction.Cancel) }) {
                        Text(
                            text = "Use another number",
                            color = AppTheme.PrimaryColor,
                            fontSize = rememberLabelFontSize()
                        )
                    }
                    return@Column
                }

                PrimaryButton(
                    text = if (isSignUp) "Sign up" else "Submit",
                    onClick = { viewModel.sent(TelegramLoginAction.Submit) },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                if (state.isMiniApp.not()) {
                    return@Column
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (isSignUp) "Or sign up with current Telegram user in the app" else "Or sign in with current Telegram user in the app",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize(),
                    lineHeight = rememberFontSize() * 1.1f,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = if (isSignUp) "Sign up with current user" else "Sign in with current user",
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

/**
 * Бэк отвечает "номер не известен боту", когда пользователь ещё не запускал бота и не отправлял контакт.
 * Регистрация отдельно не нужна: аккаунт создаётся после подтверждения кнопкой в боте.
 */
@Composable
private fun rememberToastData(
    manager: TelegramWebAppManager,
    uriHandler: UriHandler
): ToastData = remember {
    ToastData(
        message = "Telegram bot doesn't know this number. Open the bot and send your contact first",
        buttonText = "Open bot",
        onButtonClick = {
            val opened = manager.openLink(AppRemoteConfig.telegramBotLink)
            if (!opened) uriHandler.openUri(AppRemoteConfig.telegramBotLink)
        },
        duration = ToastData.Duration.Long
    )
}

@Composable
private fun InputBox(
    state: TelegramLoginState,
    viewModel: TelegramLoginVm,
    isSignUp: Boolean
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.code.isNotBlank()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isSignUp) "Please open Telegram and confirm sign up" else "Please open Telegram and confirm login",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize() * 1.3f,
                    lineHeight = rememberFontSize() * 1.4f,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Бот хранит сообщение до 30 минут для тех, кто его ещё не запускал
                Text(
                    text = "First time? Open the bot, press Start and share your contact. " +
                            "The confirmation message will arrive right after that.",
                    color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
                    fontSize = rememberLabelFontSize(),
                    lineHeight = rememberLabelFontSize() * 1.2f,
                    textAlign = TextAlign.Center
                )
            }
            return
        }
        AppTextField(
            value = state.phoneNumber,
            onValueChange = { viewModel.sent(TelegramLoginAction.SetPhoneNumber(it)) },
            label = "Phone number",
            modifier = Modifier.fillMaxWidth(),
            helperText = "Include country code, e.g., 11234567890, 3801234567890. New here? The account is created after you confirm the login in the Telegram bot."
        )
    }
}
