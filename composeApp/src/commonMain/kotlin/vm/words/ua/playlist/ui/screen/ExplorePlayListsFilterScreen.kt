package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.enums.PublicPlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter
import vm.words.ua.playlist.ui.actions.ExplorePlayListsFilterAction
import vm.words.ua.playlist.ui.states.ExplorePlayListsFilterState
import vm.words.ua.playlist.ui.vms.ExplorePlayListsFilterViewModel
import vm.words.ua.words.ui.components.ExpansionMode
import vm.words.ua.words.ui.components.SortSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorePlayListsFilterScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<ExplorePlayListsFilterViewModel>()
    val state by viewModel.state.collectAsState()

    val hasNavigatedBack = remember { mutableStateOf(false) }

    val currentFilter = navController.getParam<PublicPlayListCountFilter>() ?: PublicPlayListCountFilter()

    LaunchedEffect(Unit) {
        viewModel.send(ExplorePlayListsFilterAction.Init(currentFilter))
    }

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

    val columns = if (isNotPhoneFormat()) 2 else 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Filter Playlists",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = true,
            additionalButtonVector = Icons.Outlined.Delete,
            onAdditionalClick = { viewModel.send(ExplorePlayListsFilterAction.Clear) }
        )

        CenteredContainer(maxWidth = rememberInterfaceMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                InputMenu(columns, state, viewModel)

                PrimaryButton(
                    text = "Find",
                    onClick = { viewModel.send(ExplorePlayListsFilterAction.Find) }
                )
            }
        }


    }
}

@Composable
private fun ColumnScope.InputMenu(
    columns: Int,
    state: ExplorePlayListsFilterState,
    viewModel: ExplorePlayListsFilterViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name input
        item {
            TextInput(
                label = "Playlist name",
                value = state.name
            ) { viewModel.send(ExplorePlayListsFilterAction.ChangeName(it.orEmpty())) }
        }


        // CEFR multi-select
        item {
            MultiSelect(
                items = CEFR.entries.toList(),
                selected = state.cefrs,
                toLabel = { it.name },
                onToggle = { viewModel.send(ExplorePlayListsFilterAction.ToggleCefr(it)) },
                label = "CEFR levels",
                modifier = Modifier.fillMaxWidth()
            )
        }


        // Language selector
        item {
            SingleSelectInput(
                value = state.language,
                items = Language.entries.filter { it != Language.UNDEFINED },
                label = "Language",
                toLabel = { it.titleCase },
                showNone = true,
                noneLabel = "",
                onSelect = { viewModel.send(ExplorePlayListsFilterAction.SetLanguage(it)) }
            )
        }

        // Translate language selector
        item {
            SingleSelectInput(
                value = state.translateLanguage,
                items = Language.entries.filter { it != Language.UNDEFINED },
                label = "Translate language",
                toLabel = { it.titleCase },
                showNone = true,
                noneLabel = "",
                onSelect = { viewModel.send(ExplorePlayListsFilterAction.SetTranslateLanguage(it)) }
            )
        }

        // Sort selector
        item(span = { GridItemSpan(maxLineSpan) }) {
                SortSelector(
                    items = PublicPlaylistSortField.entries.toList(),
                    selected = state.sortField,
                    toLabel = { it.name.replace("_", " ").lowercase().replaceFirstChar { c -> c.uppercase() } },
                    showDefault = false,
                    label = "Sort by",
                    onSelect = { it?.let { field -> viewModel.send(ExplorePlayListsFilterAction.SetSortField(field)) } },
                    asc = state.asc,
                    onToggleAsc = { viewModel.send(ExplorePlayListsFilterAction.SetAsc(it)) },
                    expansionMode = ExpansionMode.Dropdown
                )
        }

        // Tags input
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StringListInput(
                    label = "Add tag",
                    items = state.tags.toList(),
                    onItemsChange = { newTags ->
                        val newTagsSet = newTags?.toSet() ?: emptySet()
                        // Find removed tags
                        val removed = state.tags - newTagsSet
                        removed.forEach { tag ->
                            viewModel.send(ExplorePlayListsFilterAction.RemoveTag(tag))
                        }
                    }
                )
            }
        }
    }
}

