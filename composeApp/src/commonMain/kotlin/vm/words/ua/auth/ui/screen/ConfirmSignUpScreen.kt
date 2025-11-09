package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import vm.words.ua.auth.ui.actions.ConfirmSignUpAction
import vm.words.ua.auth.ui.bundles.ConfirmSignBundle
import vm.words.ua.auth.ui.vms.ConfirmSignUpVm
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun ConfirmSignUpScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: ConfirmSignUpVm = rememberInstance()
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val state by viewModel.state.collectAsState()
    val bundle = navController.getParamOrThrow<ConfirmSignBundle>()

    LaunchedEffect(state.waitResult) {
        if (state.waitResult.not()) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = "Open Telegram Bot",
                    onClick = {
                        uriHandler.openUri(AppRemoteConfig.telegramBotLink)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 20.dp)
                )

                Text(
                    text = "Please check your email and confirm your registration. After that, you can log in to the application.",
                    color = AppTheme.PrimaryColor,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.size(80.dp))
            }
        }
    }
}
