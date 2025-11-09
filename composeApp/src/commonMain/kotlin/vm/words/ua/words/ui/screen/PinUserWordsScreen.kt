package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.ui.actions.PinUserWordsAction
import vm.words.ua.words.ui.bundles.PinUserWordsBundle
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

    // Initialize with words from bundle
    LaunchedEffect(Unit) {
        viewModel.sent(PinUserWordsAction.Load(bundle.words))
    }

    // Navigate back when pinning is complete
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.popBackStack()
        }
    }

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

        if (state.isInited.not()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryGreen)
            }
        }

        if (state.words.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No words to pin",
                    color = AppTheme.PrimaryColor,
                    fontSize = getFontSize()
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
                    ImageMenu(state, imagePicker)
                }

                // Sound picker
                item {
                    SoundMenu(
                        state = state,
                        soundPicker = soundPicker
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
        color = AppTheme.PrimaryGreen.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Word ${state.index + 1} of ${state.words.size}",
                color = AppTheme.PrimaryGreen,
                fontSize = getFontSize() * 0.9f,
            )
            state.currentWord?.let { word ->
                Text(
                    text = word.original,
                    color = AppTheme.PrimaryColor,
                    fontSize = getFontSize() * 1.4f,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = word.lang.titleCase,
                    color = AppTheme.PrimaryColor.copy(alpha = 0.7f),
                    fontSize = getFontSize() * 0.95f
                )
            }
        }
    }
}

@Composable
private fun ImageMenu(
    state: PinUserWordsState,
    imagePicker: PickerResultLauncher
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Custom Image",
            color = AppTheme.PrimaryColor,
            fontSize = getFontSize()
        )

        OutlinedButton(
            onClick = { imagePicker.launch() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryGreen
            )
        ) {
            Text(
                text = if (state.image != null) "Change Image" else "Select Image",
                fontSize = getFontSize()
            )
        }

        state.image?.let { file ->
            Text(
                text = "Selected: ${file.name}",
                color = AppTheme.PrimaryGreen,
                fontSize = getFontSize() * 0.85f,
                lineHeight = getFontSize()
            )
        }

        state.currentWord?.originalImage?.let {
            Text(
                text = "Original image available",
                color = AppTheme.PrimaryColor.copy(alpha = 0.6f),
                fontSize = getFontSize() * 0.85f,
                lineHeight = getFontSize()
            )
        }
    }
}

@Composable
private fun SoundMenu(
    state: PinUserWordsState,
    soundPicker: PickerResultLauncher
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Custom Sound",
            color = AppTheme.PrimaryColor,
            fontSize = getFontSize()
        )

        OutlinedButton(
            onClick = { soundPicker.launch() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryGreen
            )
        ) {
            Text(
                text = if (state.sound != null) "Change Sound" else "Select Sound",
                fontSize = getFontSize()
            )
        }

        state.sound?.let { file ->
            Text(
                text = "Selected: ${file.name}",
                color = AppTheme.PrimaryGreen,
                fontSize = getFontSize() * 0.85f,
                lineHeight = getFontSize()
            )
        }

        state.currentWord?.originalSound?.let {
            Text(
                text = "Original sound available",
                color = AppTheme.PrimaryColor.copy(alpha = 0.6f),
                fontSize = getFontSize() * 0.85f,
                lineHeight = getFontSize()
            )
        }
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
                viewModel.sent(PinUserWordsAction.SaveFiles)
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = { viewModel.sent(PinUserWordsAction.PreviousWord) },
            modifier = Modifier.weight(1f),
            enabled = state.index > 0,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryGreen
            )
        ) {
            Text("Previous", fontSize = getFontSize())
        }

        OutlinedButton(
            onClick = { viewModel.sent(PinUserWordsAction.SaveFiles) },
            modifier = Modifier.weight(1f),
            enabled = state.hasFiles,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryGreen
            )
        ) {
            Text("Save", fontSize = getFontSize())
        }


        OutlinedButton(
            onClick = {
                viewModel.sent(PinUserWordsAction.SaveFiles)
                viewModel.sent(PinUserWordsAction.NextWord)
            },
            modifier = Modifier.weight(1f),
            enabled = state.index < state.words.size - 1,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AppTheme.PrimaryGreen
            )
        ) {
            Text("Next", fontSize = getFontSize())
        }
    }
}

