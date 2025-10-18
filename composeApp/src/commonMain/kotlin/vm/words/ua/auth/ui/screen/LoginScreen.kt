package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.PrimaryButton
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.telegram_image

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (phone: String, password: String) -> Unit = { _, _ -> },
    onTelegram: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(AppTheme.PrimaryBack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", color = AppTheme.PrimaryColor)

        val phoneState = remember { mutableStateOf("") }
        val passState = remember { mutableStateOf("") }

        AppTextField(
            value = phoneState.value,
            onValueChange = { phoneState.value = it },
            label = "Phone",
            modifier = Modifier.fillMaxWidth()
        )

        AppTextField(
            value = passState.value,
            onValueChange = { passState.value = it },
            label = "Password",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Join now", color = AppTheme.PrimaryColor, modifier = Modifier.clickable { /* navigate */ })
        }

        PrimaryButton(
            text = "Sign in",
            onClick = { onLogin(phoneState.value, passState.value) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Sign in with", color = AppTheme.PrimaryColor)

        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Image(painterResource(Res.drawable.telegram_image), contentDescription = "telegram", modifier = Modifier.size(80.dp).clickable { onTelegram() })
        }
    }
}

@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
