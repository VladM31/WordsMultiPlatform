package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.CenteredContainer
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.ui.actions.PlayListFilterAction
import vm.words.ua.playlist.ui.vms.PlayListFilterViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete

@Composable
fun PlayListFilterScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListFilterViewModel>()
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Track if we already navigated back to prevent multiple triggers
    val hasNavigatedBack = remember { mutableStateOf(false) }

    // Get current filter from navigation params
    val currentFilter = navController.getParam<PlayListCountFilter>() ?: PlayListCountFilter()

    // Initialize with current filter
    LaunchedEffect(Unit) {
        viewModel.send(PlayListFilterAction.Init(currentFilter))
    }

    // Navigate back when filter is applied - only once
    LaunchedEffect(state.isEnd) {
        if (state.isEnd && !hasNavigatedBack.value) {
            hasNavigatedBack.value = true
            try {
                val filter = viewModel.toFilter()
                navController.popBackStack(returnParam = filter)
            } catch (e: Exception) {
                e.printStackTrace()
                navController.popBackStack()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Playlist Filter",
            showBackButton = true,
            onBackClick = {
                navController.popBackStack()
            },
            showAdditionalButton = true,
            additionalButtonImage = painterResource(Res.drawable.delete),
            onAdditionalClick = {
                viewModel.send(PlayListFilterAction.Clear)
            }
        )

        BoxWithConstraints(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            val scaleFactor = getScaleFactor(maxWidth)
            val inputTextSize = rememberFontSize(scaleFactor)
            val labelTextSize = inputTextSize * 0.95f
            val titleTextSize = inputTextSize * 1.2f
            val buttonTextSize = (30 * scaleFactor).sp
            val fieldHeight = (56 * scaleFactor).dp
            val buttonHeight = (56 * scaleFactor).dp
            val horizontalPadding = (16 * scaleFactor).dp
            val verticalPadding = (24 * scaleFactor).dp
            val spacing = (24 * scaleFactor).dp
            val buttonPadding = (16 * scaleFactor).dp

            CenteredContainer(
                maxWidth = 600.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        // Name input
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = { viewModel.send(PlayListFilterAction.ChangeName(it)) },
                            label = { Text("Playlist name", fontSize = labelTextSize, color = AppTheme.PrimaryColor) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = fieldHeight),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppTheme.PrimaryColor,
                                unfocusedBorderColor = AppTheme.PrimaryColor.copy(alpha = 0.5f),
                                focusedTextColor = AppTheme.PrimaryColor,
                                unfocusedTextColor = AppTheme.PrimaryColor,
                                cursorColor = AppTheme.PrimaryColor
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = inputTextSize),
                            singleLine = true
                        )

                        // Count section
                        Text(
                            text = "Count",
                            fontSize = titleTextSize,
                            color = AppTheme.PrimaryColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Start and End count inputs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
                        ) {
                            OutlinedTextField(
                                value = state.startCount,
                                onValueChange = { viewModel.send(PlayListFilterAction.ChangeStartCount(it)) },
                                label = { Text("Start", fontSize = labelTextSize, color = AppTheme.PrimaryColor) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = fieldHeight),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.PrimaryColor,
                                    unfocusedBorderColor = AppTheme.PrimaryColor.copy(alpha = 0.5f),
                                    focusedTextColor = AppTheme.PrimaryColor,
                                    unfocusedTextColor = AppTheme.PrimaryColor,
                                    cursorColor = AppTheme.PrimaryColor
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = inputTextSize),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = state.endCount,
                                onValueChange = { viewModel.send(PlayListFilterAction.ChangeEndCount(it)) },
                                label = { Text("End", fontSize = labelTextSize, color = AppTheme.PrimaryColor) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = fieldHeight),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.PrimaryColor,
                                    unfocusedBorderColor = AppTheme.PrimaryColor.copy(alpha = 0.5f),
                                    focusedTextColor = AppTheme.PrimaryColor,
                                    unfocusedTextColor = AppTheme.PrimaryColor,
                                    cursorColor = AppTheme.PrimaryColor
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = inputTextSize),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }
                    }

                    // Find button at the bottom
                    Button(
                        onClick = { viewModel.send(PlayListFilterAction.Find) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(buttonPadding)
                            .height(buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.PrimaryColor,
                            contentColor = AppTheme.PrimaryBack
                        )
                    ) {
                        Text(
                            text = "Find",
                            fontSize = buttonTextSize,
                            color = AppTheme.PrimaryBack
                        )
                    }
                }
            }
        }
    }
}
