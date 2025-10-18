package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.PrimaryButton

@Composable
fun TelegramLoginScreen(
    modifier: Modifier = Modifier,
    onSubmit: (phoneNumber: String) -> Unit = {},
    onOpenTelegram: () -> Unit = {},
    isLoading: Boolean = false,
    showTelegramPrompt: Boolean = false
) {
    var phoneNumberState by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Telegram Login",
                color = AppTheme.PrimaryColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 70.dp)
            )

            if (!showTelegramPrompt) {
                AppTextField(
                    value = phoneNumberState,
                    onValueChange = {
                        if (it.length <= 15) {
                            phoneNumberState = it
                        }
                    },
                    label = "Input phone number",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showTelegramPrompt) {
                Text(
                    text = "Please open Telegram and confirm login",
                    color = AppTheme.PrimaryColor,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        }

        // Кнопки внизу экрана
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showTelegramPrompt) {
                PrimaryButton(
                    text = "Submit",
                    onClick = { onSubmit(phoneNumberState) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = phoneNumberState.isNotEmpty() && !isLoading
                )
            } else {
                PrimaryButton(
                    text = "Open Telegram",
                    onClick = onOpenTelegram,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Индикатор загрузки
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.PrimaryBack.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AppTheme.PrimaryColor
                )
            }
        }
    }
}
