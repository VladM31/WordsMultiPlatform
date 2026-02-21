package vm.words.ua.words.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.models.enums.UserWordSortBy
import vm.words.ua.words.domain.models.filters.UserWordFilter
import vm.words.ua.words.ui.actions.UserWordFilterAction
import vm.words.ua.words.ui.bundles.UserWordFilterBundle
import vm.words.ua.words.ui.components.SortSelector
import vm.words.ua.words.ui.states.UserWordFilterState
import vm.words.ua.words.ui.vms.UserWordFilterVm


@Composable
fun UserWordFilterScreen(
    navController: SimpleNavController,
    viewModel: UserWordFilterVm = rememberInstance<UserWordFilterVm>()
) {
    val state by viewModel.state.collectAsState()

    // Local UI states
    var sortByExpanded by remember { mutableStateOf(false) }

    val bundle = navController.getParamOrThrow<UserWordFilterBundle>();

    // Initialize with current filter
    LaunchedEffect(Unit) {
        viewModel.sent(UserWordFilterAction.Init(bundle.filter))
    }

    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.popBackStack(
                returnParam = UserWordFilterBundle(filter = state.toFilter())
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        AppToolBar(
            title = "Word Filter",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = true,
            additionalButtonVector = Icons.Outlined.Delete,
            onAdditionalClick = {
                viewModel.sent(UserWordFilterAction.Clear)
            }
        )

        val columns = if (isNotPhoneFormat()) 2 else 1

        CenteredContainer(
            modifier = Modifier.weight(1f).fillMaxSize(),
            maxWidth = rememberInterfaceMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    TextInput(label = "Original", value = state.original) {
                        viewModel.sent(UserWordFilterAction.SetOriginal(it))
                    }
                }

                item {
                    SingleSelectInput(
                        value = state.lang,
                        items = Language.entries.filter { it != Language.UNDEFINED },
                        label = "Original language",
                        toLabel = { it.titleCase },
                        showNone = true,
                        noneLabel = "",
                        onSelect = { viewModel.sent(UserWordFilterAction.SetOriginalLang(it)) }
                    )
                }

                item {
                    TextInput(label = "Translate", value = state.translate) {
                        viewModel.sent(UserWordFilterAction.SetTranslate(it))
                    }
                }

                item {
                    SingleSelectInput(
                        value = state.translateLang,
                        items = Language.entries.filter { it != Language.UNDEFINED },
                        label = "Translate language",
                        toLabel = { it.titleCase },
                        showNone = true,
                        noneLabel = "",
                        onSelect = { viewModel.sent(UserWordFilterAction.SetTranslateLang(it)) }
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MultiSelect(
                            items = CEFR.entries.toList(),
                            selected = state.cefrs,
                            toLabel = { it.name },
                            onToggle = { level -> viewModel.sent(UserWordFilterAction.SetCefr(level)) },
                            label = "CEFR levels",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    SortSelector(
                        items = UserWordSortBy.entries.toList(),
                        selected = state.sortBy,
                        toLabel = { it.titleCase },
                        showDefault = false,
                        label = "Sort by",
                        expanded = sortByExpanded,
                        onExpandedChange = { sortByExpanded = it },
                        onSelect = { selectedSort ->
                            viewModel.sent(
                                UserWordFilterAction.SetSortBy(
                                    selectedSort ?: UserWordSortBy.CREATED_AT
                                )
                            )
                        },
                        asc = state.asc,
                        onToggleAsc = { viewModel.sent(UserWordFilterAction.SetAsc(it)) },
                        expansionMode = vm.words.ua.words.ui.components.ExpansionMode.Dropdown
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StringListInput(
                            label = "Add category",
                            items = state.categories,
                            onItemsChange = { viewModel.sent(UserWordFilterAction.SetCategories(it)) }
                        )
                    }
                }

            }
        }

        PrimaryButton(
            text = "Find",
            onClick = {
                navController.popBackStack(
                    returnParam = UserWordFilterBundle(filter = state.toFilter())
                )
            }
        )
    }
}

private fun UserWordFilterState.toFilter(): UserWordFilter {
    return UserWordFilter(
        original = this.original,
        translate = this.translate,

        languages = listOfNotNull(this.lang),
        translateLanguages = listOfNotNull(this.translateLang),
        categories = this.categories,
        cefrs = this.cefrs,
        sortField = this.sortBy,
        asc = this.asc
    )
}