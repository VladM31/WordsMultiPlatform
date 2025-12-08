package vm.words.ua.auth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.auth.ui.actions.LoginAction
import vm.words.ua.auth.ui.hints.LoginScreenHintStep
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.utils.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.hints.ui.utils.viewHint
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.telegram_image

@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    maxWidth: Dp,
    currentHintStep: ViewHintStep? = null,
    onJoinNowClick: () -> Unit = {},
    onTelegramClick: () -> Unit = {}
) {
    val scaleFactor = getScaleFactor(maxWidth)
    val textWeight = 0.75f

    val phoneState = viewModel.state.map { it.phoneNumber }.distinctUntilChanged().collectAsState(initial = "")
    val passState = viewModel.state.map { it.password }.distinctUntilChanged().collectAsState(initial = "")

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppTextField(
            value = phoneState.value,
            onValueChange = { viewModel.sent(LoginAction.SetPhoneNumber(it ))},
            label = "Phone",
            boxMaxWidth = maxWidth,
            modifier = Modifier.fillMaxWidth()
                .viewHint(LoginScreenHintStep.PHONE_NUMBER, currentHintStep),
            helperText = "Include country code, e.g., 11234567890, 3801234567890"
        )

        Spacer(modifier = Modifier.size((12 * scaleFactor).dp))

        AppTextField(
            value = passState.value,
            onValueChange = { viewModel.sent(LoginAction.SetPassword(it ))},
            label = "Password",
            modifier = Modifier.fillMaxWidth()
                .viewHint(LoginScreenHintStep.PASSWORD, currentHintStep),
            boxMaxWidth = maxWidth,
            isPassword = true
        )

        Spacer(modifier = Modifier.size((12 * scaleFactor).dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Sign up",
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(scaleFactor * textWeight),
                modifier = Modifier
                    .viewHint(LoginScreenHintStep.SIGN_UP_LINK, currentHintStep)
                    .clickable { onJoinNowClick() }
            )
        }

        Spacer(modifier = Modifier.size((12 * scaleFactor).dp))

        PrimaryButton(
            text = "Sign in",
            onClick = { viewModel.sent(LoginAction.Submit) },
            modifier = Modifier.fillMaxWidth()
                .padding(4.dp)
                .viewHint(LoginScreenHintStep.LOGIN_BUTTON, currentHintStep)
        )

        Spacer(modifier = Modifier.size((8 * scaleFactor).dp))

        Text(
            text = "Sign in with",
            color = AppTheme.PrimaryColor,
            fontSize = rememberFontSize(scaleFactor * textWeight)
        )

        Spacer(modifier = Modifier.size((8 * scaleFactor).dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(Res.drawable.telegram_image),
                contentDescription = "telegram",
                modifier = Modifier.size(rememberIconSize() * 1.5f).clickable { onTelegramClick() }
                    .viewHint(LoginScreenHintStep.TELEGRAM_LOGIN_BUTTON, currentHintStep)
            )
        }
    }
}
