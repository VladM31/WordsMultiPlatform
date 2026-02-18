package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.auth.ui.actions.TelegramSignUpAction
import vm.words.ua.auth.ui.bundles.ConfirmSignBundle
import vm.words.ua.auth.ui.hints.TelegramSignUpScreenHintController
import vm.words.ua.auth.ui.hints.TelegramSignUpScreenHintStep
import vm.words.ua.auth.ui.hints.createTelegramSignUpScreenHintController
import vm.words.ua.auth.ui.states.TelegramSignUpState
import vm.words.ua.auth.ui.vms.TelegramSignUpViewModel
import vm.words.ua.core.domain.models.enums.Currency
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.utils.hints.ui.components.SimpleHintHost
import vm.words.ua.utils.hints.ui.utils.viewHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelegramSignUpScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: TelegramSignUpViewModel = rememberInstance()

) {
    val state by viewModel.state.collectAsState()
    val hintController = createTelegramSignUpScreenHintController()

    LaunchedEffect(state.success) {
        if (state.success.not()) {
            return@LaunchedEffect
        }
        navController.navigateAndClearCurrent(
            Screen.ConfirmSignUp, ConfirmSignBundle(
                phoneNumber = state.phoneNumber,
                password = state.password
            )
        )
    }


    SimpleHintHost(
        onNext = hintController.doNext
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppTheme.PrimaryBack)
        ) {
            AppToolBar(
                title = "Sign Up",
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                showAdditionalButton = true,
                additionalButtonVector = Icons.Filled.Info,
                onAdditionalClick = {
                    navController.navigate(Screen.Policy)
                },
                currentStepHint = hintController.currentStep,
                additionalButtonStepHint = TelegramSignUpScreenHintStep.PRIVACY_POLICY
            )

            CenteredContainer(maxWidth = rememberInterfaceMaxWidth() * 1.4f) {
                Content(state, viewModel, hintController)
            }

            state.error?.let {
                ErrorMessageBox(message = it)
            }
        }
    }
}

@Composable
private fun Content(
    state: TelegramSignUpState,
    viewModel: TelegramSignUpViewModel,
    hintController: TelegramSignUpScreenHintController
) {
    val columns = if (isNotPhoneFormat()) 2 else 1

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                AppTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.sent(TelegramSignUpAction.SetPhoneNumber(it)) },
                    label = "Phone number",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(TelegramSignUpScreenHintStep.PHONE_NUMBER, hintController.currentStep),
                    helperText = "Include country code, e.g., 11234567890, 3801234567890"
                )
            }

            item {
                AppTextField(
                    value = state.password,
                    onValueChange = { viewModel.sent(TelegramSignUpAction.SetPassword(it)) },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(TelegramSignUpScreenHintStep.PASSWORD, hintController.currentStep),
                    isPassword = true,
                    helperText = "Password must be between 8 and 60 characters long"
                )
            }

            item {
                AppTextField(
                    value = state.firstName,
                    onValueChange = { viewModel.sent(TelegramSignUpAction.SetFirstName(it)) },
                    label = "First name",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(TelegramSignUpScreenHintStep.FIRST_NAME, hintController.currentStep)
                )
            }

            item {
                AppTextField(
                    value = state.lastName,
                    onValueChange = { viewModel.sent(TelegramSignUpAction.SetLastName(it)) },
                    label = "Last name",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(TelegramSignUpScreenHintStep.LAST_NAME, hintController.currentStep)
                )
            }


            item {
                SingleSelectInput(
                    value = state.currency,
                    items = Currency.entries.toList(),
                    label = "Currency",
                    toLabel = { it.name },
                    showNone = false,
                    noneLabel = "",
                    onSelect = { viewModel.sent(TelegramSignUpAction.SetCurrency(it ?: Currency.USD)) },
                    modifier = Modifier.viewHint(
                        TelegramSignUpScreenHintStep.CURRENCY_SELECTION,
                        hintController.currentStep
                    ),
                    helperText = "Select your preferred currency for transactions and pricing."
                )

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .viewHint(TelegramSignUpScreenHintStep.AGREEMENT_CHECKBOX, hintController.currentStep),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Checkbox(
                checked = state.agreed,
                onCheckedChange = { viewModel.sent(TelegramSignUpAction.SetAgreed(it)) },
                colors = CheckboxDefaults.colors(
                    checkedColor = AppTheme.PrimaryColor,
                    uncheckedColor = AppTheme.PrimaryColor,
                    checkmarkColor = AppTheme.PrimaryBack
                )
            )
            Text(
                text = "I agree to the terms of service",
                color = AppTheme.PrimaryColor,
                fontSize = 16.sp
            )
        }

        PrimaryButton(
            text = "Submit",
            onClick = {
                viewModel.sent(TelegramSignUpAction.Submit)
            },
            modifier = Modifier.width(300.dp)
                .padding(bottom = 10.dp)
                .align(CenterHorizontally)
                .viewHint(TelegramSignUpScreenHintStep.SIGN_UP_BUTTON, hintController.currentStep),
            enabled = state.agreed
        )
    }
}
