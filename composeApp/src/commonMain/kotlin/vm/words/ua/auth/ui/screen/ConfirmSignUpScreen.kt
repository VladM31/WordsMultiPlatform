package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.auth.ui.actions.ConfirmSignUpAction
import vm.words.ua.auth.ui.bundles.ConfirmSignBundle
import vm.words.ua.auth.ui.vms.ConfirmSignUpVm
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.navigation.rememberParamOrThrow

@Composable
fun ConfirmSignUpScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: ConfirmSignUpVm = rememberInstance()
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val state = viewModel.state.collectAsState()
    val bundle = navController.rememberParamOrThrow<ConfirmSignBundle>()

    LaunchedEffect(state.value.waitResult) {
        if (state.value.waitResult.not()) {
            navController.navigateAndClear(Screen.Home)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sent(
            ConfirmSignUpAction.Init(bundle.phoneNumber, bundle.password)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Confirmation",
            onBackClick = { navController.popBackStack() }
        )

        // Redesigned central card with clearer typography and actions
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = AppTheme.PrimaryBack, // keep consistent background inside the card
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Decorative icon circle
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(
                                AppTheme.PrimaryColor.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = AppTheme.PrimaryColor,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Confirm your account",
                        color = AppTheme.PrimaryColor,
                        fontSize = rememberFontSize() * 1.6f,
                        lineHeight = rememberFontSize() * 1.65f,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We sent a confirmation message. Please follow the link in the message to finish registration.",
                        color = AppTheme.PrimaryColor.copy(alpha = 0.9f),
                        fontSize = rememberFontSize() * 1.05f,
                        lineHeight = rememberFontSize() * 1.1f,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .padding(top = 4.dp, bottom = 12.dp)
                    )

                    // Show contact info (phone) so user knows where the message was sent
                    Text(
                        text = "Sent to: ${bundle.phoneNumber}",
                        color = AppTheme.PrimaryColor,
                        fontSize = rememberFontSize() * 0.95f,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 18.dp)
                    )

                    PrimaryButton(
                        text = "Open Telegram Bot",
                        onClick = {
                            uriHandler.openUri(AppRemoteConfig.telegramBotLink)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Text(text = "Back to Login", color = AppTheme.PrimaryColor)
                    }

                }
            }
        }
    }
}
