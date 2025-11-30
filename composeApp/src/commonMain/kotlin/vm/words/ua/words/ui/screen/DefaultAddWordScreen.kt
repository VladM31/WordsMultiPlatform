package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.SingleSelectInput
import vm.words.ua.core.ui.components.TextInput
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.ui.actions.DefaultAddWordAction
import vm.words.ua.words.ui.components.SelectImageMenu
import vm.words.ua.words.ui.components.SelectSoundMenu
import vm.words.ua.words.ui.states.DefaultAddWordState
import vm.words.ua.words.ui.vms.DefaultAddWordVm

@Composable
fun DefaultAddWordScreen(
    navController: SimpleNavController,
    viewModel: DefaultAddWordVm = rememberInstance()
) {

    val state by viewModel.state.collectAsState()
    val columns = if (isNotPhoneFormat()) 2 else 1

    val imagePicker = rememberFilePickerLauncher(
        type = FileKitType.File(setOf("jpg", "jpeg", "png", "webp", "gif")),
        onResult = { file -> viewModel.sent(DefaultAddWordAction.SetImage(file)) }
    )

    val soundPicker = rememberFilePickerLauncher(
        type = FileKitType.File(setOf("mp3", "wav")),
        onResult = { file -> viewModel.sent(DefaultAddWordAction.SetSound(file)) }
    )

    LaunchedEffect(Unit) {
        viewModel.sent(DefaultAddWordAction.Init(null))
    }

    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigateAndClearCurrent(Screen.UserWords)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Add word",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        if (state.isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                        .align(Alignment.Center),
                    color = AppTheme.PrimaryGreen
                )
            }

            return@Column
        }



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
                    TextInput(label = "Original word", value = state.originalWord) {
                        viewModel.sent(DefaultAddWordAction.SetOriginalWord(it.orEmpty()))
                    }
                }

                item {
                    SingleSelectInput(
                        value = state.language,
                        items = Language.entries.filter { it != Language.UNDEFINED },
                        label = "Original language",
                        toLabel = { it.titleCase },
                        showNone = true,
                        noneLabel = "",
                        onSelect = { viewModel.sent(DefaultAddWordAction.SetOriginalLang(it ?: Language.ENGLISH)) }
                    )
                }

                item {
                    TextInput(label = "Translate", value = state.translation) {
                        viewModel.sent(DefaultAddWordAction.SetTranslation(it.orEmpty()))
                    }
                }

                item {
                    SingleSelectInput(
                        value = state.translationLanguage,
                        items = Language.entries.filter { it != Language.UNDEFINED },
                        label = "Translate language",
                        toLabel = { it.titleCase },
                        showNone = true,
                        noneLabel = "",
                        onSelect = {
                            viewModel.sent(
                                DefaultAddWordAction.SetTranslationLanguage(
                                    it ?: Language.UKRAINIAN
                                )
                            )
                        }
                    )
                }

                item {
                    TextInput(label = "Description", value = state.description) {
                        viewModel.sent(DefaultAddWordAction.SetDescription(it.orEmpty()))
                    }
                }

                item {
                    SingleSelectInput(
                        value = state.cefr,
                        items = CEFR.entries,
                        label = "CEFR Level",
                        toLabel = { it.name },
                        showNone = true,
                        noneLabel = "",
                        onSelect = { viewModel.sent(DefaultAddWordAction.SetCefr(it ?: CEFR.A1)) }
                    )
                }

                item {
                    TextInput(label = "Category", value = state.category) {
                        viewModel.sent(DefaultAddWordAction.SetCategory(it.orEmpty()))
                    }
                }

                item {
                    SoundSwitch(state = state, viewModel = viewModel)
                }

                item {
                    SelectImageMenu(
                        image = state.image,
                        title = "Custom Image",
                        imagePicker = imagePicker,
                    ) {
                        viewModel.sent(DefaultAddWordAction.SetImage(null))
                    }
                }

                item {
                    SelectSoundMenu(
                        sound = state.sound,
                        soundPicker = soundPicker,
                        title = "Custom Sound",
                        isPlay = state.isPlaying,
                        onPlayClick = {
                            viewModel.sent(DefaultAddWordAction.PlaySound)
                        }
                    ) {
                        viewModel.sent(DefaultAddWordAction.SetSound(null))
                    }
                }
            }

            // Apply button at the bottom
            Button(
                onClick = {
                    viewModel.sent(DefaultAddWordAction.Add)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryGreen,
                    contentColor = AppTheme.PrimaryBack
                )
            ) {
                Text("Add", fontSize = rememberFontSize())
            }
        }
    }

    state.errorMessage?.let {
        ErrorMessageBox(it)
    }
}

@Composable
private fun SoundSwitch(
    state: DefaultAddWordState,
    viewModel: DefaultAddWordVm
) {
    val scale = getScaleFactor()
    val minHeight = remember(scale) {
        (56 * scale).dp
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Switch(
                checked = state.needSound,
                onCheckedChange = {
                    viewModel.sent(DefaultAddWordAction.SetNeedSound(it))
                }
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Generate sound",
                color = AppTheme.PrimaryGreen,
                fontSize = rememberFontSize(),
                lineHeight = rememberFontSize() * 1.1f
            )
        }
    }
}