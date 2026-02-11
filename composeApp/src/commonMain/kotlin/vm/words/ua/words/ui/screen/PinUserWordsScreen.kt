package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.platform.isWeb
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.bundles.PlayListDetailsBundle
import vm.words.ua.playlist.ui.components.SelectPlayListDialog
import vm.words.ua.words.ui.actions.PinUserWordsAction
import vm.words.ua.words.ui.bundles.PinUserWordsBundle
import vm.words.ua.words.ui.components.SelectImageMenu
import vm.words.ua.words.ui.components.SelectSoundMenu
import vm.words.ua.words.ui.states.PinUserWordsState
import vm.words.ua.words.ui.vms.PinUserWordsViewModel

@Composable
fun PinUserWordsScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PinUserWordsViewModel>()
    val state by viewModel.state.collectAsState()

    val bundle = navController.getParamOrThrow<PinUserWordsBundle>()

    var showPlayListSelector by remember { mutableStateOf(false) }

    // File pickers for current word
    val imagePicker = rememberFilePickerLauncher(
        type = FileKitType.File(setOf("jpg", "jpeg", "png", "webp", "gif")),
        onResult = { file -> viewModel.sent(PinUserWordsAction.SetImage(file)) }
    )

    val soundPicker = rememberFilePickerLauncher(
        type = FileKitType.File(setOf("mp3", "wav")),
        onResult = { file -> viewModel.sent(PinUserWordsAction.SetSound(file)) }
    )

    val columns = if (isNotPhoneFormat()) 2 else 1

    // Initialize with words from bundle
    LaunchedEffect(Unit) {
        viewModel.sent(PinUserWordsAction.Load(bundle.words))
    }

    // Navigate to UserWords when pinning is complete
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.navigateAndClear(Screen.UserWords, Screen.Home)
        }
    }

    // Navigate to PlayList details when words added to playlist
    LaunchedEffect(state.navigateToPlayListId) {
        state.navigateToPlayListId?.let { playListId ->
            navController.navigateAndClear(Screen.Home)
            navController.navigate(
                Screen.PlayListDetails,
                PlayListDetailsBundle(playListId)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Pin Words",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        // Show completion menu dialog when pinning is done
        if (state.showCompletionMenu) {
            CompletionMenuDialog(
                onGoToUserWords = {
                    viewModel.sent(PinUserWordsAction.GoToUserWords)
                },
                onAddToPlaylist = {
                    showPlayListSelector = true
                }
            )
        }

        // Show playlist selector dialog
        if (showPlayListSelector) {
            SelectPlayListDialog(
                onDismiss = { showPlayListSelector = false },
                onPin = { playlistId ->
                    viewModel.sent(PinUserWordsAction.AddToPlayList(playlistId))
                    showPlayListSelector = false
                }
            )
        }

        if (state.isLoading || state.isInited.not()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
            }
            return@Column
        }

        if (state.words.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No words to pin",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize()
                )
            }
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
                // Word info card
                item(span = { GridItemSpan(maxLineSpan) }) {
                    TopMenu(state)
                }

                // Image picker
                item {
                    ImageMenu(state, imagePicker, viewModel)
                }

                // Sound picker
                item {
                    SoundMenu(
                        state = state,
                        soundPicker = soundPicker,
                        viewModel = viewModel
                    )
                }
            }

            // Navigation and action buttons at bottom
            BottomMenu(
                viewModel = viewModel,
                state = state
            )
        }
    }
}

@Composable
private fun TopMenu(
    state: PinUserWordsState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AppTheme.PrimaryColor.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Word ${state.index + 1} of ${state.words.size}",
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize() * 0.9f,
            )
            state.currentWord?.let { word ->
                Text(
                    text = word.original,
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize() * 1.4f,
                    lineHeight = rememberFontSize() * 1.5f,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = word.lang.titleCase,
                    color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
                    fontSize = rememberFontSize() * 0.95f
                )
            }
        }
    }
}

@Composable
private fun ImageMenu(
    state: PinUserWordsState,
    imagePicker: PickerResultLauncher,
    viewModel: PinUserWordsViewModel
) {

    val underText = state.currentWord?.originalImage?.let {
        "Original image available"
    }

    SelectImageMenu(
        title = "Custom Image",
        image = state.image,
        imagePicker = imagePicker,
        underText = underText
    ) {
        viewModel.sent(PinUserWordsAction.SetImage(null))
    }
}

@Composable
private fun SoundMenu(
    state: PinUserWordsState,
    soundPicker: PickerResultLauncher,
    viewModel: PinUserWordsViewModel
) {
    val underText = state.currentWord?.originalImage?.let {
        "Original sound available"
    }

    SelectSoundMenu(
        sound = state.sound,
        soundPicker = soundPicker,
        title = "Custom Sound",
        underText = underText,
        isPlay = state.isPlay,
        onPlayClick = {
            viewModel.sent(PinUserWordsAction.PlaySound)
        }
    ) {
        viewModel.sent(PinUserWordsAction.SetSound(null))
    }
}

@Composable
private fun BottomMenu(
    viewModel: PinUserWordsViewModel,
    state: PinUserWordsState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Previous/Next navigation
        WordNavigator(viewModel, state)

        // Save and Pin button
        PrimaryButton(
            text = "Pin All Words",
            onClick = {
                viewModel.sent(PinUserWordsAction.Pin)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun WordNavigator(
    viewModel: PinUserWordsViewModel,
    state: PinUserWordsState
) {
    val platform = currentPlatform()
    val leftArrow = if (platform.isWeb) "<" else "⬅"
    val rightArrow = if (platform.isWeb) ">" else "⮕"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = { viewModel.sent(PinUserWordsAction.PreviousWord) },
            modifier = Modifier.weight(1f),
            enabled = state.index > 0,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryColor
            )
        ) {
            Text(leftArrow, fontSize = rememberFontSize())
        }

        OutlinedButton(
            onClick = { viewModel.sent(PinUserWordsAction.SaveFiles) },
            modifier = Modifier.weight(2f),
            enabled = state.hasUpdate,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryColor
            )
        ) {
            Text("Save", fontSize = rememberFontSize())
        }


        OutlinedButton(
            onClick = {
                viewModel.sent(PinUserWordsAction.SaveFiles)
                viewModel.sent(PinUserWordsAction.NextWord)
            },
            modifier = Modifier.weight(1f),
            enabled = state.index < state.words.size - 1,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryColor
            )
        ) {
            Text(rightArrow, fontSize = rememberFontSize())
        }
    }
}

@Composable
private fun CompletionMenuDialog(
    onGoToUserWords: () -> Unit,
    onAddToPlaylist: () -> Unit
) {
    Dialog(onDismissRequest = { }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = AppTheme.PrimaryBack
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 280.dp, max = 400.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Words Pinned Successfully!",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize() * 1.2f,
                    lineHeight = rememberFontSize() * 1.3,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "What would you like to do next?",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize(),
                    lineHeight = rememberFontSize() * 1.1,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onAddToPlaylist,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Add to Playlist",
                            color = AppTheme.PrimaryBack,
                            fontSize = rememberFontSize()
                        )
                    }

                    OutlinedButton(
                        onClick = onGoToUserWords,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppTheme.PrimaryColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Go to My Words",
                            fontSize = rememberFontSize()
                        )
                    }
                }
            }
        }
    }
}
