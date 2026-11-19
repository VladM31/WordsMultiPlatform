package vm.words.ua.settings.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import vm.words.ua.auth.domain.models.OwnershipProvider
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.ui.states.ToastData
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.settings.ui.actions.ProfileAction
import vm.words.ua.settings.ui.states.ProfileDialog
import vm.words.ua.settings.ui.states.ProfileState
import vm.words.ua.settings.ui.vm.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: SimpleNavController,
    viewModel: ProfileViewModel = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    val toaster = rememberToast()
    var menuOpen by remember { mutableStateOf(false) }

    LaunchedEffect(state.message) {
        state.message?.let {
            toaster.show(ToastData(message = it))
            viewModel.sent(ProfileAction.DismissMessage)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        // На широком окне данные слева, кнопки справа; на узком кнопки уходят в меню тулбара
        val isWide = maxWidth >= WIDE_LAYOUT_MIN_WIDTH
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
        AppToolBar(
            title = "Profile",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = isWide.not(),
            additionalButtonVector = Icons.Filled.MoreVert,
            onAdditionalClick = { menuOpen = true }
        )
            if (isWide.not()) {
                ProfileMenu(
                    expanded = menuOpen,
                    state = state,
                    onDismiss = { menuOpen = false },
                    onAction = { menuOpen = false; viewModel.sent(it) }
                )
            }
            CenteredContainer(maxWidth = if (isWide) WIDE_LAYOUT_CONTENT_WIDTH else rememberInterfaceMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                if (isWide) {
                    Row(
                        // Без IntrinsicSize.Min: он не учитывает перенос длинного email и режет карточку
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        ProfileCard(state, modifier = Modifier.weight(1f))
                        ProfileActionsPanel(state, viewModel, modifier = Modifier.weight(1f))
                    }
                } else {
                    // Profile information card
                    ProfileCard(state)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.weight(1f))

                // Delete account button
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    TextButton(
                        onClick = { viewModel.sent(ProfileAction.OpenDialog(ProfileDialog.DELETE)) },
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
    }
    }

    when (state.dialog) {
        ProfileDialog.NONE -> Unit
        ProfileDialog.EDIT_NAME -> EditNameDialog(state, viewModel)
        ProfileDialog.PASSWORD -> PasswordDialog(state, viewModel)
        ProfileDialog.DELETE -> DeleteAccountDialog(state, viewModel)
    }

    // Error messages shown as dialog, the form stays open so the user can fix the input
    state.errorMessage?.let { message ->
        ErrorMessageBox(
            message = message,
            onDismiss = { viewModel.sent(ProfileAction.DismissError) }
        )
    }

    AppToast(toaster)
}

private val WIDE_LAYOUT_MIN_WIDTH = 840.dp
private val WIDE_LAYOUT_CONTENT_WIDTH = 960.dp
private const val MIN_VALUE_SCALE = 0.5f

/** Блок кнопок справа от данных пользователя на широком окне */
@Composable
private fun ProfileActionsPanel(
    state: ProfileState,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.PrimaryBack),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Account",
                fontSize = rememberLabelFontSize() * 0.9,
                fontWeight = FontWeight.Bold,
                color = AppTheme.PrimaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth()
            )
            AccountActionRow(
                text = "Edit name",
                icon = Icons.Outlined.Edit,
                isFilled = true,
                onClick = { viewModel.sent(ProfileAction.OpenDialog(ProfileDialog.EDIT_NAME)) }
            )
            AccountActionRow(
                text = "Password",
                icon = Icons.Outlined.Lock,
                onClick = { viewModel.sent(ProfileAction.OpenDialog(ProfileDialog.PASSWORD)) }
            )
            if (state.isGoogleAvailable) {
                AccountActionRow(
                    text = "Link Google",
                    icon = LinkIcon,
                    enabled = !state.isLoading,
                    onClick = { viewModel.sent(ProfileAction.LinkGoogle) }
                )
            }
        }
    }
}

/**
 * Кнопка-пилюля блока Account: круглая иконка слева, подпись и стрелка справа.
 * Главное действие залито цветом, остальные с обводкой.
 */
@Composable
private fun AccountActionRow(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isFilled: Boolean = false,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(50)
    val contentColor = if (isFilled) AppTheme.PrimaryBack else AppTheme.PrimaryColor
    val iconBackground = if (isFilled) {
        AppTheme.PrimaryBack.copy(alpha = 0.2f)
    } else {
        AppTheme.PrimaryColor.copy(alpha = 0.15f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .then(
                if (isFilled) {
                    Modifier.background(AppTheme.PrimaryColor)
                } else {
                    Modifier.border(2.dp, AppTheme.PrimaryColor, shape)
                }
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = text,
            color = contentColor,
            fontSize = rememberFontSize(),
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.padding(end = 8.dp).size(24.dp)
        )
    }
}

/** Иконка цепочки (Material "link"): в базовом наборе иконок её нет, поэтому рисуем вручную */
private val LinkIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Link",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).addPath(
        pathData = PathParser().parsePathString(
            "M3.9,12c0,-1.71 1.39,-3.1 3.1,-3.1h4V7H7c-2.76,0 -5,2.24 -5,5s2.24,5 5,5h4v-1.9H7c-1.71,0 " +
                    "-3.1,-1.39 -3.1,-3.1zM8,13h8v-2H8v2zM17,7h-4v1.9h4c1.71,0 3.1,1.39 3.1,3.1s-1.39,3.1 " +
                    "-3.1,3.1h-4V17h4c2.76,0 5,-2.24 5,-5s-2.24,-5 -5,-5z"
        ).toNodes(),
        fill = SolidColor(Color.Black)
    ).build()
}

/**
 * Меню действий в правой кнопке тулбара. Якорь невидимый и стоит сразу под тулбаром у правого края,
 * в тех же границах, что и сам тулбар, поэтому меню открывается под кнопкой.
 */
@Composable
private fun ProfileMenu(
    expanded: Boolean,
    state: ProfileState,
    onDismiss: () -> Unit,
    onAction: (ProfileAction) -> Unit
) {
    CenteredContainer(
        maxWidth = rememberInterfaceMaxWidth(),
        modifier = Modifier.fillMaxWidth().height(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopEnd)) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismiss,
                offset = DpOffset((-12).dp, 0.dp),
                modifier = Modifier.background(AppTheme.PrimaryBack).widthIn(min = 200.dp)
            ) {
                ProfileMenuItem("Edit name", Icons.Filled.Edit) {
                    onAction(ProfileAction.OpenDialog(ProfileDialog.EDIT_NAME))
                }
                ProfileMenuItem("Password", Icons.Filled.Lock) {
                    onAction(ProfileAction.OpenDialog(ProfileDialog.PASSWORD))
                }
                if (state.isGoogleAvailable) {
                    ProfileMenuItem("Link Google", Icons.Filled.AccountCircle, enabled = !state.isLoading) {
                        onAction(ProfileAction.LinkGoogle)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                color = AppTheme.PrimaryColor,
                fontSize = rememberLabelFontSize(),
                maxLines = 1,
                softWrap = false
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.PrimaryColor
            )
        },
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
private fun EditNameDialog(state: ProfileState, viewModel: ProfileViewModel) {
    var firstName by remember { mutableStateOf(state.firstName) }
    var lastName by remember { mutableStateOf(state.lastName) }

    ProfileDialogFrame(
        title = "Edit name",
        confirmText = "Save",
        isLoading = state.isLoading,
        onConfirm = { viewModel.sent(ProfileAction.SaveName(firstName, lastName)) },
        onDismiss = { viewModel.sent(ProfileAction.DismissDialog) }
    ) {
        AppTextField(
            value = firstName,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { firstName = it },
            label = "First name"
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppTextField(
            value = lastName,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { lastName = it },
            label = "Last name"
        )
    }
}

@Composable
private fun PasswordDialog(state: ProfileState, viewModel: ProfileViewModel) {
    var newPassword by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }

    ProfileDialogFrame(
        title = "Password",
        confirmText = "Save",
        isLoading = state.isLoading,
        onConfirm = {
            viewModel.sent(ProfileAction.SetPassword(newPassword, oldPassword.ifEmpty { null }))
        },
        onDismiss = { viewModel.sent(ProfileAction.DismissDialog) }
    ) {
        // Без пароля старый пароль спрашивать нечего: достаточно подтвердить владение
        val hasPassword = state.hasPassword != false
        Text(
            text = if (hasPassword) {
                "Enter your current password, or confirm ownership if you forgot it."
            } else {
                "Your account has no password yet. Confirm ownership to set the first one."
            },
            fontSize = rememberLabelFontSize(),
            lineHeight = rememberLabelFontSize() * 1.1,
            textAlign = TextAlign.Center,
            color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        AppTextField(
            value = newPassword,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { newPassword = it },
            label = "New password",
            isPassword = true,
            helperText = "8 to 60 characters"
        )
        if (hasPassword) {
            Spacer(modifier = Modifier.height(12.dp))
            AppTextField(
                value = oldPassword,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { oldPassword = it },
                label = "Current password",
                isPassword = true,
                helperText = "Forgot it? Leave empty and confirm ownership below"
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ProofSection(state, viewModel, newPassword = newPassword)
    }
}

@Composable
private fun DeleteAccountDialog(state: ProfileState, viewModel: ProfileViewModel) {
    var password by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    ProfileDialogFrame(
        title = "Delete Account",
        confirmText = "Delete",
        confirmColor = AppTheme.PrimaryRed,
        isLoading = state.isLoading,
        onConfirm = {
            viewModel.sent(
                ProfileAction.DeleteAccount(
                    password = password.ifEmpty { null },
                    reason = reason.ifBlank { null }
                )
            )
        },
        onDismiss = { viewModel.sent(ProfileAction.DismissDialog) }
    ) {
        Text(
            text = "This action is irreversible. All your data will be permanently deleted.",
            fontSize = rememberLabelFontSize(),
            lineHeight = rememberLabelFontSize() * 1.1,
            textAlign = TextAlign.Center,
            color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Без пароля его не просим: аккаунт подтверждается через Google или Telegram
        if (state.hasPassword != false) {
            AppTextField(
                value = password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                helperText = "Forgot it? Leave empty and confirm ownership below"
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        ProofSection(state, viewModel)
        Spacer(modifier = Modifier.height(12.dp))
        AppTextField(
            value = reason,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { reason = it },
            label = "Reason (optional)",
            singleLine = false
        )
    }
}

/**
 * Подтверждение владения без пароля: свежий вход Google или Telegram.
 * В Telegram достаточно нажать Yes в боте: приложение само узнаёт об этом и идёт дальше.
 * Внутри Mini App бот не нужен совсем.
 *
 * @param newPassword введённый новый пароль: после подтверждения через Telegram он отправится сам
 */
@Composable
private fun ProofSection(
    state: ProfileState,
    viewModel: ProfileViewModel,
    newPassword: String? = null
) {
    val telegramManager: TelegramWebAppManager = rememberInstance()
    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val proof = state.proof
        if (proof != null) {
            val via = if (proof.provider == OwnershipProvider.GOOGLE) "Google" else "Telegram"
            Text(
                text = "Ownership confirmed with $via",
                fontSize = rememberLabelFontSize(),
                color = AppTheme.PrimaryColor,
                textAlign = TextAlign.Center
            )
            return
        }

        if (state.telegramProofCode != null) {
            Text(
                text = "Waiting for your Yes in the Telegram bot…",
                fontSize = rememberLabelFontSize(),
                lineHeight = rememberLabelFontSize() * 1.1,
                color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = AppTheme.PrimaryColor,
                strokeWidth = 2.dp
            )
            TextButton(onClick = {
                val opened = telegramManager.openLink(AppRemoteConfig.telegramBotLink)
                if (!opened) uriHandler.openUri(AppRemoteConfig.telegramBotLink)
            }) {
                Text(text = "Open bot", fontSize = rememberLabelFontSize())
            }
            return
        }

        Text(
            text = "Confirm ownership with",
            fontSize = rememberLabelFontSize(),
            color = AppTheme.PrimaryColor.copy(alpha = 0.7f)
        )
        Row(horizontalArrangement = Arrangement.Center) {
            if (state.isGoogleAvailable) {
                TextButton(
                    onClick = { viewModel.sent(ProfileAction.ProveWithGoogle) },
                    enabled = !state.isLoading
                ) {
                    Text(text = "Google", fontSize = rememberLabelFontSize())
                }
            }
            if (state.isTelegramProofAvailable) {
                TextButton(
                    onClick = { viewModel.sent(ProfileAction.ProveWithTelegram(newPassword)) },
                    enabled = !state.isLoading
                ) {
                    Text(text = "Telegram", fontSize = rememberLabelFontSize())
                }
            }
        }
    }
}

@Composable
private fun ProfileDialogFrame(
    title: String,
    confirmText: String,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmColor: Color = AppTheme.PrimaryColor,
    content: @Composable ColumnScope.() -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            // Диалог может не поместиться по высоте (особенно с подтверждением владения): прокручиваем
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                content()
                if (isLoading) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.PrimaryColor
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.textButtonColors(contentColor = confirmColor)
            ) {
                Text(text = confirmText, fontWeight = FontWeight.Bold, fontSize = rememberLabelFontSize())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", fontSize = rememberLabelFontSize())
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ProfileCard(state: ProfileState, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
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
                fontSize = rememberLabelFontSize() * 0.9,
                fontWeight = FontWeight.Bold,
                color = AppTheme.PrimaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
                    .fillMaxWidth()
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
                    valueColor = AppTheme.PrimaryColor
                )
            }

            // Phone (if exists)
            if (!state.phoneNumber.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileInfoItem(
                    label = "Phone",
                    value = state.phoneNumber.orEmpty(),
                    valueColor = AppTheme.PrimaryColor
                )
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
    label: String,
    value: String,
    valueColor: Color = AppTheme.PrimaryColor
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
        // Значение в одну строку: если не помещается (длинный email), шрифт уменьшается до нужного
        val baseSize = rememberFontSize()
        var scale by remember(value, baseSize) { mutableStateOf(1f) }
        Text(
            text = value,
            fontSize = baseSize * scale,
            lineHeight = baseSize * scale * 1.25f,
            fontWeight = FontWeight.Normal,
            color = valueColor,
            maxLines = 1,
            softWrap = false,
            onTextLayout = { result ->
                if (result.hasVisualOverflow && scale > MIN_VALUE_SCALE) {
                    scale = (scale * 0.92f).coerceAtLeast(MIN_VALUE_SCALE)
                }
            }
        )
    }
}
