package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.auth.ui.actions.SignUpAction
import vm.words.ua.auth.ui.bundles.ConfirmSignBundle
import vm.words.ua.auth.ui.hints.SignUpScreenHintStep
import vm.words.ua.auth.ui.hints.createSignUpScreenHintController
import vm.words.ua.auth.ui.vms.SignUpViewModel
import vm.words.ua.core.domain.models.enums.Currency
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.utils.hints.ui.components.SimpleHintHost
import vm.words.ua.utils.hints.ui.utils.viewHint
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.info_in_circle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = rememberInstance()

) {
    val state by viewModel.state.collectAsState()
    val hintController = createSignUpScreenHintController()

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
                additionalButtonImage = painterResource(Res.drawable.info_in_circle),
                onAdditionalClick = {
                    navController.navigate(Screen.Policy)
                },
                currentStepHint = hintController.currentStep,
                additionalButtonStepHint = SignUpScreenHintStep.PRIVACY_POLICY
            )

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
                            onValueChange = { viewModel.sent(SignUpAction.SetPhoneNumber(it)) },
                            label = "Phone number",
                            modifier = Modifier.fillMaxWidth()
                                .viewHint(SignUpScreenHintStep.PHONE_NUMBER, hintController.currentStep)
                        )
                    }

                    item {
                        AppTextField(
                            value = state.password,
                            onValueChange = { viewModel.sent(SignUpAction.SetPassword(it)) },
                            label = "Password",
                            modifier = Modifier.fillMaxWidth()
                                .viewHint(SignUpScreenHintStep.PASSWORD, hintController.currentStep),
                            isPassword = true
                        )
                    }

                    item {
                        AppTextField(
                            value = state.firstName,
                            onValueChange = { viewModel.sent(SignUpAction.SetFirstName(it)) },
                            label = "First name",
                            modifier = Modifier.fillMaxWidth()
                                .viewHint(SignUpScreenHintStep.FIRST_NAME, hintController.currentStep)
                        )
                    }

                    item {
                        AppTextField(
                            value = state.lastName,
                            onValueChange = { viewModel.sent(SignUpAction.SetLastName(it)) },
                            label = "Last name",
                            modifier = Modifier.fillMaxWidth()
                                .viewHint(SignUpScreenHintStep.LAST_NAME, hintController.currentStep)
                        )
                    }

                    item {
                        AppTextField(
                            value = state.email.orEmpty(),
                            onValueChange = { viewModel.sent(SignUpAction.SetEmail(it)) },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth()
                                .viewHint(SignUpScreenHintStep.EMAIL, hintController.currentStep)
                        )
                    }

                    item {
                        SingleSelectInput(
                            value = state.currency,
                            items = Currency.entries.toList(),
                            label = "Currency",
                            toLabel = { it.name },
                            showNone = true,
                            noneLabel = "",
                            onSelect = { viewModel.sent(SignUpAction.SetCurrency(it ?: Currency.USD)) },
                            modifier = Modifier.viewHint(
                                SignUpScreenHintStep.CURRENCY_SELECTION,
                                hintController.currentStep
                            )
                        )

                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .viewHint(SignUpScreenHintStep.AGREEMENT_CHECKBOX, hintController.currentStep),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Checkbox(
                        checked = state.agreed,
                        onCheckedChange = { viewModel.sent(SignUpAction.SetAgreed(it)) },
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
                        viewModel.sent(SignUpAction.Submit)
                    },
                    modifier = Modifier.width(300.dp)
                        .padding(bottom = 10.dp)
                        .align(CenterHorizontally)
                        .viewHint(SignUpScreenHintStep.SIGN_UP_BUTTON, hintController.currentStep),
                    enabled = state.agreed
                )
            }

            state.error?.let {
                ErrorMessageBox(message = it)
            }
        }
    }
}
