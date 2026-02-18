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
import vm.words.ua.auth.ui.actions.GoogleSignUpAction
import vm.words.ua.auth.ui.hints.GoogleSignUpScreenHintController
import vm.words.ua.auth.ui.hints.GoogleSignUpScreenHintStep
import vm.words.ua.auth.ui.hints.createGoogleSignUpScreenHintController
import vm.words.ua.auth.ui.states.GoogleSignUpState
import vm.words.ua.auth.ui.vms.GoogleSignUpViewModel
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
fun GoogleSignUpScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: GoogleSignUpViewModel = rememberInstance()

) {
    val state by viewModel.state.collectAsState()
    val hintController = createGoogleSignUpScreenHintController()

    LaunchedEffect(state.success) {
        if (state.success.not()) {
            return@LaunchedEffect
        }
        navController.navigateAndClearCurrent(
            Screen.Home
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
                additionalButtonStepHint = GoogleSignUpScreenHintStep.PRIVACY_POLICY
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
    state: GoogleSignUpState,
    viewModel: GoogleSignUpViewModel,
    hintController: GoogleSignUpScreenHintController
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
                    value = state.firstName,
                    onValueChange = { viewModel.sent(GoogleSignUpAction.SetFirstName(it)) },
                    label = "First name",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(GoogleSignUpScreenHintStep.FIRST_NAME, hintController.currentStep)
                )
            }

            item {
                AppTextField(
                    value = state.lastName,
                    onValueChange = { viewModel.sent(GoogleSignUpAction.SetLastName(it)) },
                    label = "Last name",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(GoogleSignUpScreenHintStep.LAST_NAME, hintController.currentStep)
                )
            }
            item {
                AppTextField(
                    value = state.password,
                    onValueChange = { viewModel.sent(GoogleSignUpAction.SetPassword(it)) },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(GoogleSignUpScreenHintStep.PASSWORD, hintController.currentStep),
                    isPassword = true,
                    helperText = "Password must be between 8 and 60 characters long"
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
                    onSelect = { viewModel.sent(GoogleSignUpAction.SetCurrency(it ?: Currency.USD)) },
                    modifier = Modifier.viewHint(
                        GoogleSignUpScreenHintStep.CURRENCY_SELECTION,
                        hintController.currentStep
                    ),
                    helperText = "Select your preferred currency for transactions and pricing."
                )

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .viewHint(GoogleSignUpScreenHintStep.AGREEMENT_CHECKBOX, hintController.currentStep),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Checkbox(
                checked = state.agreed,
                onCheckedChange = { viewModel.sent(GoogleSignUpAction.SetAgreed(it)) },
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
                viewModel.sent(GoogleSignUpAction.Submit)
            },
            modifier = Modifier.width(300.dp)
                .padding(bottom = 10.dp)
                .align(CenterHorizontally)
                .viewHint(GoogleSignUpScreenHintStep.SIGN_UP_BUTTON, hintController.currentStep),
            enabled = state.agreed
        )
    }
}
