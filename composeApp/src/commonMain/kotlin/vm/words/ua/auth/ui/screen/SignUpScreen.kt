package vm.words.ua.auth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.navigation.SimpleNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    onSignUp: (
        phone: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        currency: String
    ) -> Unit = { _, _, _, _, _, _ -> }
) {
    val scrollState = rememberScrollState()

    var phoneState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var firstNameState by remember { mutableStateOf("") }
    var lastNameState by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("USD") }
    val currencies = listOf("USD", "EUR", "UAH")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Sign Up",
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppTextField(
                            value = phoneState,
                            onValueChange = { phoneState = it },
                            label = "Phone number",
                            modifier = Modifier.fillMaxWidth()
                        )

                        AppTextField(
                            value = passwordState,
                            onValueChange = { passwordState = it },
                            label = "Password",
                            modifier = Modifier.fillMaxWidth(),
                            isPassword = true
                        )

                        AppTextField(
                            value = emailState,
                            onValueChange = { emailState = it },
                            label = "Email",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppTextField(
                            value = firstNameState,
                            onValueChange = { firstNameState = it },
                            label = "First name",
                            modifier = Modifier.fillMaxWidth()
                        )

                        AppTextField(
                            value = lastNameState,
                            onValueChange = { lastNameState = it },
                            label = "Last name",
                            modifier = Modifier.fillMaxWidth()
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            AppTextField(
                                value = selectedCurrency,
                                onValueChange = {},
                                label = "Currency",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(AppTheme.PrimaryBack)
                            ) {
                                currencies.forEach { currency ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = currency,
                                                color = AppTheme.PrimaryColor
                                            )
                                        },
                                        onClick = {
                                            selectedCurrency = currency
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
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
                    if (agreedToTerms) {
                        onSignUp(
                            phoneState,
                            passwordState,
                            emailState,
                            firstNameState,
                            lastNameState,
                            selectedCurrency
                        )
                        navController.navigate("confirm_signup")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = agreedToTerms
            )
        }
    }
}
