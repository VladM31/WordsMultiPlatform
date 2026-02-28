package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.utils.rememberColumnCount
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.navigation.rememberParamOrThrow
import vm.words.ua.words.ui.actions.WordEditAction
import vm.words.ua.words.ui.bundles.ReloadWordDetailsBundle
import vm.words.ua.words.ui.bundles.WordEditBundle
import vm.words.ua.words.ui.components.SelectImageMenu
import vm.words.ua.words.ui.components.SelectSoundMenu
import vm.words.ua.words.ui.states.WordEditState
import vm.words.ua.words.ui.vms.WordEditVm

@Composable
fun WordEditScreen(
    navController: SimpleNavController,
    viewModel: WordEditVm = rememberInstance(),
) {
    val bundle = navController.rememberParamOrThrow<WordEditBundle>()
    val state by viewModel.state.collectAsState()

    val imagePicker = rememberFilePickerLauncher(
        type = FileKitType.File(setOf("jpg", "jpeg", "png", "webp", "gif")),
        onResult = { file -> viewModel.sent(WordEditAction.SetImage(file)) }
    )

    val soundPicker = rememberFilePickerLauncher(
        type = FileKitType.File(setOf("mp3", "wav")),
        onResult = { file -> viewModel.sent(WordEditAction.SetSound(file)) }
    )

    LaunchedEffect(Unit) {
        viewModel.sent(WordEditAction.Init(bundle.userWord))
    }

    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.popBackStack(ReloadWordDetailsBundle)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Edit word",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        if (state.isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                    color = AppTheme.PrimaryColor
                )
            }
            return@Column
        }

        InputMenu(rememberColumnCount(), state, imagePicker, viewModel, soundPicker)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            PrimaryButton(
                text = "Save",
                onClick = { viewModel.sent(WordEditAction.Save) },
                enabled = state.canSave
            )
        }
    }

    state.errorMessage?.let { ErrorMessageBox(it) }
}

@Composable
private fun ColumnScope.InputMenu(
    columns: Int,
    state: WordEditState,
    imagePicker: PickerResultLauncher,
    viewModel: WordEditVm,
    soundPicker: PickerResultLauncher
) {
    CenteredContainer(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize(),
        maxWidth = rememberInterfaceMaxWidth() * 1.5f
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                if (state.isSubscriptionActive.not()) {
                    return@item
                }
                ImageBlock(
                    state = state,
                    imagePicker = imagePicker,
                    onRemoveImage = { viewModel.sent(WordEditAction.SetImage(null)) }
                )
            }

            item {
                if (state.isSubscriptionActive.not()) {
                    return@item
                }
                AudioBlock(
                    state = state,
                    soundPicker = soundPicker,
                    onRemoveSound = { viewModel.sent(WordEditAction.SetSound(null)) },
                    onPlayClick = { viewModel.sent(WordEditAction.PlaySound) }
                )
            }

            item {
                if (state.isCustomWord.not()) {
                    return@item
                }
                MainFields(state = state, viewModel = viewModel)
            }

            item {
                if (state.isCustomWord.not()) {
                    return@item
                }
                AdditionalFields(state = state, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun ImageBlock(
    state: WordEditState,
    imagePicker: PickerResultLauncher,
    onRemoveImage: () -> Unit,
) {

    Column {
        SelectImageMenu(
            image = if (state.isSubscriptionActive) state.image else null,
            imagePicker = imagePicker,
            title = "Image",
            underText = when {
                state.image == null && !state.imageFileName.isNullOrBlank() -> "Current available"
                else -> null
            },
            onRemoveImage = onRemoveImage,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AudioBlock(
    state: WordEditState,
    soundPicker: PickerResultLauncher,
    onRemoveSound: () -> Unit,
    onPlayClick: () -> Unit = {},
) {

    Column {
        SelectSoundMenu(
            sound = if (state.isSubscriptionActive) state.sound else null,
            soundPicker = soundPicker,
            title = "Audio",
            underText = when {
                !state.isSubscriptionActive -> "Subscription required to change audio"
                state.sound == null && !state.soundFileName.isNullOrBlank() -> "Current available"
                else -> null
            },
            onRemoveSound = onRemoveSound,
            onPlayClick = onPlayClick,
            isPlay = state.isPlaying,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MainFields(
    state: WordEditState,
    viewModel: WordEditVm,
) {
    val isEditable = state.isCustomWord

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        EditField(
            value = state.original,
            label = "Original",
            readOnly = !isEditable,
            error = state.originalError,
            onValueChange = { viewModel.sent(WordEditAction.SetOriginal(it)) }
        )

        SingleSelectInput(
            value = state.lang,
            items = Language.entries.filter { it != Language.UNDEFINED },
            label = "Original language",
            toLabel = { it.titleCase },
            showNone = false,
            noneLabel = "",
            enabled = isEditable,
            onSelect = { it?.let { lang -> viewModel.sent(WordEditAction.SetLang(lang)) } }
        )

        EditField(
            value = state.translate,
            label = "Translation",
            readOnly = !isEditable,
            error = state.translateError,
            onValueChange = { viewModel.sent(WordEditAction.SetTranslate(it)) }
        )

        SingleSelectInput(
            value = state.translateLang,
            items = Language.entries.filter { it != Language.UNDEFINED },
            label = "Translation language",
            toLabel = { it.titleCase },
            showNone = false,
            noneLabel = "",
            enabled = isEditable,
            onSelect = { it?.let { lang -> viewModel.sent(WordEditAction.SetTranslateLang(lang)) } }
        )

        EditField(
            value = state.category,
            label = "Category",
            readOnly = !isEditable,
            error = state.categoryError,
            onValueChange = { viewModel.sent(WordEditAction.SetCategory(it)) }
        )
    }
}

@Composable
private fun AdditionalFields(
    state: WordEditState,
    viewModel: WordEditVm,
) {
    val isEditable = state.isCustomWord

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SingleSelectInput(
            value = state.cefr,
            items = CEFR.entries,
            label = "CEFR Level",
            toLabel = { it.name },
            showNone = false,
            noneLabel = "",
            enabled = isEditable,
            onSelect = { it?.let { cefr -> viewModel.sent(WordEditAction.SetCefr(cefr)) } }
        )



        EditField(
            value = state.description,
            label = "Description",
            readOnly = !isEditable,
            error = state.descriptionError,
            minLines = 3,
            onValueChange = { viewModel.sent(WordEditAction.SetDescription(it)) }
        )
    }
}

@Composable
private fun EditField(
    value: String,
    label: String,
    readOnly: Boolean,
    error: String?,
    onValueChange: (String) -> Unit,
    minLines: Int = 1,
) {
    val fontSize = rememberFontSize()
    val labelFontSize = rememberLabelFontSize()
    val hasError = error != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = if (hasError) AppTheme.Error else AppTheme.PrimaryColor,
                fontSize = labelFontSize
            )
        },
        readOnly = readOnly,
        isError = hasError,
        minLines = minLines,
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = fontSize),
        supportingText = error?.let {
            {
                Text(
                    text = it,
                    color = AppTheme.Error,
                    fontSize = labelFontSize * 0.85f
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (hasError) AppTheme.Error else AppTheme.PrimaryColor,
            unfocusedBorderColor = if (hasError) AppTheme.Error else AppTheme.PrimaryColor.copy(alpha = 0.5f),
            focusedTextColor = AppTheme.PrimaryColor,
            unfocusedTextColor = AppTheme.PrimaryColor,
            cursorColor = AppTheme.PrimaryColor,
            errorBorderColor = AppTheme.Error,
            errorLabelColor = AppTheme.Error,
            errorSupportingTextColor = AppTheme.Error,
        )
    )
}