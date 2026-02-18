package vm.words.ua.auth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
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
import vm.words.ua.core.ui.icons.TelegramIcon
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.utils.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.hints.ui.utils.viewHint
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.google_icon

@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    currentHintStep: ViewHintStep? = null,
    onJoinNowClick: () -> Unit = {},
    onTelegramClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
    showGoogleSignIn: Boolean = false
) {
    val scaleFactor = rememberScaleFactor()
    val iconSize = rememberIconSize() * 1.2f
    val phoneState = viewModel.state.map { it.username }.distinctUntilChanged().collectAsState(initial = "")
    val passState = viewModel.state.map { it.password }.distinctUntilChanged().collectAsState(initial = "")

    if (viewModel.state.value.telegramLoginSession != null) {
        onTelegramClick()
        return
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextField(
                value = phoneState.value,
                onValueChange = { viewModel.sent(LoginAction.SetUsername(it)) },
                label = "Phone or Email",

                modifier = Modifier.fillMaxWidth()
                    .viewHint(LoginScreenHintStep.PHONE_NUMBER_OR_EMAIL, currentHintStep),
                helperText = "Phone number must include country code, e.g., 11234567890, 3801234567890"
            )

            Spacer(modifier = Modifier.size((12 * scaleFactor).dp))

            AppTextField(
                value = passState.value,
                onValueChange = { viewModel.sent(LoginAction.SetPassword(it)) },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
                    .viewHint(LoginScreenHintStep.PASSWORD, currentHintStep),
                isPassword = true
            )

            Spacer(modifier = Modifier.size((12 * scaleFactor).dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    color = AppTheme.SecondaryText,
                    fontSize = rememberLabelFontSize()
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = AppTheme.PrimaryColor.copy(alpha = 0.1f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape((8 * scaleFactor).dp)
                        )
                        .clickable { onJoinNowClick() }
                        .viewHint(LoginScreenHintStep.SIGN_UP_LINK, currentHintStep)
                        .padding(horizontal = (12 * scaleFactor).dp, vertical = (6 * scaleFactor).dp)
                ) {
                    Text(
                        text = "Sign up",
                        color = AppTheme.PrimaryColor,
                        fontSize = rememberLabelFontSize(),
                        modifier = Modifier
                    )
                }
            }

            Spacer(modifier = Modifier.size((16 * scaleFactor).dp))

            SignInButton(viewModel, currentHintStep, scaleFactor)

            Spacer(modifier = Modifier.size((8 * scaleFactor).dp))

            Text(
                text = "Sign in with",
                color = AppTheme.PrimaryColor,
                fontSize = rememberLabelFontSize()
            )

            Spacer(modifier = Modifier.size((8 * scaleFactor).dp))

            OAuthSignIn(onTelegramClick, currentHintStep, iconSize, showGoogleSignIn, onGoogleClick)
            // Small theme icon in top-right corner
            IconButton(
                onClick = onThemeClick,
                modifier = Modifier
                    .size((32 * scaleFactor).dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Change Theme",
                    tint = AppTheme.PrimaryColor.copy(alpha = 0.6f),
                    modifier = Modifier.size((20 * scaleFactor).dp)
                )
            }
        }


    }
}

@Composable
private fun OAuthSignIn(
    onTelegramClick: () -> Unit,
    currentHintStep: ViewHintStep?,
    iconSize: Dp,
    showGoogleSignIn: Boolean,
    onGoogleClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier

                .background(AppTheme.PrimaryColor, shape = CircleShape)
                .clickable { onTelegramClick() }
                .viewHint(LoginScreenHintStep.TELEGRAM_LOGIN_BUTTON, currentHintStep),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = TelegramIcon,
                contentDescription = "Telegram Sign-In",
                tint = AppTheme.PrimaryBack,
                modifier = Modifier.size(iconSize)
            )
        }

        if (!showGoogleSignIn) {
            return@Row
        }
        Spacer(modifier = Modifier.size(16.dp))

        GoogleSignInButton(
            onClick = onGoogleClick,
            iconSize = iconSize,
            currentHintStep = currentHintStep
        )
    }
}

@Composable
private fun SignInButton(
    viewModel: LoginViewModel,
    currentHintStep: ViewHintStep?,
    scaleFactor: Float
) {
    Button(
        onClick = { viewModel.sent(LoginAction.Submit) },
        modifier = Modifier
            .fillMaxWidth()
            .viewHint(LoginScreenHintStep.LOGIN_BUTTON, currentHintStep),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.PrimaryColor,
            contentColor = AppTheme.PrimaryBack
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = (4 * scaleFactor).dp,
            pressedElevation = (8 * scaleFactor).dp,
            disabledElevation = 0.dp
        ),
        shape = RoundedCornerShape((12 * scaleFactor).dp),
        contentPadding = PaddingValues(
            vertical = (7 * scaleFactor).dp
        )
    ) {
        Text(
            text = "Sign in",
            fontSize = rememberFontSize(),
            modifier = Modifier
        )
    }
}


@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    currentHintStep: ViewHintStep? = null
) {
    Box(
        modifier = modifier
            .size(iconSize)
            .background(AppTheme.PrimaryColor, shape = CircleShape)
            .clickable { onClick() }
            .viewHint(LoginScreenHintStep.GMAIL_LOGIN_BUTTON, currentHintStep),
        contentAlignment = Alignment.Center
    ) {
        // Using Material Icon for Google logo
        Icon(
            painter = painterResource(Res.drawable.google_icon),
            contentDescription = "Google Sign-In",
            tint = AppTheme.PrimaryBack,
            modifier = Modifier.size(iconSize)
        )
    }
}

