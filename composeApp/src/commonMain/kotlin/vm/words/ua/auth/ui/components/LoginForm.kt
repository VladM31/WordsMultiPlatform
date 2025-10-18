package vm.words.ua.auth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.auth.ui.actions.LoginAction
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.PrimaryButton
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.telegram_image

@Composable
fun ColumnScope.LoginForm(
    viewModel: LoginViewModel,
    onJoinNowClick: () -> Unit = {},
    onTelegramClick: () -> Unit = {},
    showTelegramButton: Boolean = true
) {
    val phoneState = viewModel.state.map { it.phoneNumber }.distinctUntilChanged().collectAsState(initial = "")
    val passState = viewModel.state.map { it.password }.distinctUntilChanged().collectAsState(initial = "")

    AppTextField(
        value = phoneState.value,
        onValueChange = { viewModel.sent(LoginAction.SetPhoneNumber(it ))},
        label = "Phone",
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.size(12.dp))

    AppTextField(
        value = passState.value,
        onValueChange = { viewModel.sent(LoginAction.SetPassword(it ))},
        label = "Password",
        modifier = Modifier.fillMaxWidth(),
        isPassword = true
    )

    Spacer(modifier = Modifier.size(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Join now",
            color = AppTheme.PrimaryColor,
            modifier = Modifier.clickable { onJoinNowClick() }
        )
    }

    Spacer(modifier = Modifier.size(12.dp))

    PrimaryButton(
        text = "Sign in",
        onClick = { viewModel.sent(LoginAction.Submit) },
        modifier = Modifier.fillMaxWidth()
    )

    if (showTelegramButton) {
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Sign in with", color = AppTheme.PrimaryColor)

        Spacer(modifier = Modifier.size(8.dp))

        Image(
            painterResource(Res.drawable.telegram_image),
            contentDescription = "telegram",
            modifier = Modifier.size(80.dp).clickable { onTelegramClick() }
        )
    }
}
