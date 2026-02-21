package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.CenteredContainer
import vm.words.ua.core.ui.components.PrimaryButton
import vm.words.ua.core.ui.components.TextInput
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.enums.PlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.ui.actions.PlayListFilterAction
import vm.words.ua.playlist.ui.vms.PlayListFilterViewModel
import vm.words.ua.words.ui.components.ExpansionMode
import vm.words.ua.words.ui.components.SortSelector

@Composable
fun PlayListFilterScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListFilterViewModel>()
    val state by viewModel.state.collectAsState()

    // Track if we already navigated back to prevent multiple triggers
    val hasNavigatedBack = remember { mutableStateOf(false) }

    // Get current filter from navigation params
    val currentFilter = navController.getParam<PlayListCountFilter>() ?: PlayListCountFilter()

    val buttonPadding = (16 * rememberScaleFactor()).dp
    val buttonHeight = (56 * rememberScaleFactor()).dp

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
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolBar(
            title = "Playlist Filter",
            showBackButton = true,
            onBackClick = {
                navController.popBackStack()
            },
            showAdditionalButton = true,
            additionalButtonVector = Icons.Outlined.Delete,
            onAdditionalClick = {
                viewModel.send(PlayListFilterAction.Clear)
            }
        )
        val columns = if (isNotPhoneFormat()) 2 else 1

        CenteredContainer(
            maxWidth = rememberInterfaceMaxWidth(),
            modifier = Modifier.weight(1f).fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    TextInput(
                        label = "Playlist name",
                        value = state.name
                    ) { viewModel.send(PlayListFilterAction.ChangeName(it.orEmpty())) }
                }

                item {
                    TextInput(
                        label = "Count from",
                        value = state.startCount,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) { viewModel.send(PlayListFilterAction.ChangeStartCount(it.orEmpty())) }
                }

                item {
                    TextInput(
                        label = "Count to",
                        value = state.endCount,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) { viewModel.send(PlayListFilterAction.ChangeEndCount(it.orEmpty())) }
                }

                // Sort selector
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SortSelector(
                        items = PlaylistSortField.entries.toList(),
                        selected = state.sortField,
                        toLabel = { it.name.replace("_", " ").lowercase().replaceFirstChar { c -> c.uppercase() } },
                        showDefault = false,
                        label = "Sort by",
                        onSelect = { it?.let { field -> viewModel.send(PlayListFilterAction.SetSortField(field)) } },
                        asc = state.asc,
                        onToggleAsc = { viewModel.send(PlayListFilterAction.SetAsc(it)) },
                        expansionMode = ExpansionMode.Dropdown
                    )
                }
            }
        }

        PrimaryButton(
            text = "Find",
            onClick = {
                viewModel.send(PlayListFilterAction.Find)
            }
        )
    }
}
