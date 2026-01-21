package vm.words.ua.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppTextField
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.settings.ui.actions.ProfileAction
import vm.words.ua.settings.ui.vm.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: SimpleNavController,
    viewModel: ProfileViewModel = rememberInstance()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Profile",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile information card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.PrimaryBack
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header
                    Text(
                        text = "Personal Information",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.PrimaryColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // First name
                    ProfileInfoItem(
                        label = "First Name",
                        value = state.firstName
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Last name
                    ProfileInfoItem(
                        label = "Last Name",
                        value = state.lastName
                    )

                    // Email (if exists)
                    if (!state.email.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileInfoItem(
                            label = "Email",
                            value = state.email.orEmpty(),
                            valueColor = AppTheme.PrimaryGreen
                        )
                    }

                    // Phone (if exists)
                    if (!state.phoneNumber.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileInfoItem(
                            label = "Phone",
                            value = state.phoneNumber.orEmpty(),
                            valueColor = AppTheme.PrimaryGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Delete account button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TextButton(
                    onClick = { viewModel.sent(ProfileAction.OpenDeleteAccountDialog) },
                    modifier = Modifier
                        .widthIn(max = 350.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppTheme.PrimaryRed.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = "Delete Account",
                        fontSize = rememberLabelFontSize(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Error messages shown as dialog
    state.errorMessage?.let { message ->
        ErrorMessageBox(
            message = message,
            onDismiss = { viewModel.sent(ProfileAction.DismissDeleteAccountDialog) }
        )
    }

    if (state.tryToDelete.not()) {
        return
    }

    var password by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { viewModel.sent(ProfileAction.DismissDeleteAccountDialog) },
        title = {
            Text(
                text = "Delete Account",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "This action is irreversible. All your data will be permanently deleted.",
                    fontSize = rememberLabelFontSize(),
                    lineHeight = rememberLabelFontSize() * 1.1,
                    textAlign = TextAlign.Center,
                    color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AppTextField(
                    value = password,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { password = it },
                    label = "Password",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppTextField(
                    value = reason,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { reason = it },
                    label = "Reason (optional)",
                    singleLine = false
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.sent(
                        ProfileAction.DeleteAccount(
                            password = password,
                            reason = reason.ifBlank { null }
                        )
                    )
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.PrimaryRed
                )
            ) {
                Text(text = "Delete", fontWeight = FontWeight.Bold, fontSize = rememberLabelFontSize())
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.sent(ProfileAction.DismissDeleteAccountDialog) }
            ) {
                Text(text = "Cancel", fontSize = rememberLabelFontSize())
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ProfileInfoItem(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = AppTheme.PrimaryColor
) {
    Column {
        Text(
            text = label,
            fontSize = rememberLabelFontSize(),
            fontWeight = FontWeight.Medium,
            color = AppTheme.PrimaryColor.copy(alpha = 0.6f),
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = rememberFontSize(),
            fontWeight = FontWeight.Normal,
            color = valueColor
        )
    }
}