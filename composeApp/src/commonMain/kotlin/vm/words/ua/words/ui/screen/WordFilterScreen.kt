package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.utils.isNotPhoneFormat
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.mappers.toWordFilter
import vm.words.ua.words.domain.models.enums.WordSortBy
import vm.words.ua.words.ui.actions.WordFilterAction
import vm.words.ua.words.ui.bundles.WordFilterBundle
import vm.words.ua.words.ui.components.SortSelector
import vm.words.ua.words.ui.vms.WordFilterViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordFilterScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<WordFilterViewModel>()
    val state by viewModel.state.collectAsState()

    // Local UI states
    var sortByExpanded by remember { mutableStateOf(false) }

    val bundle = navController.getParam<WordFilterBundle>();

    // Initialize with current filter
    LaunchedEffect(Unit) {
        bundle?.let {
            viewModel.sent(
                WordFilterAction.Init(
                    filterId = it.filterId,
                    value = it.filter
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Word Filter",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = true,
            additionalButtonImage = painterResource(Res.drawable.delete),
            onAdditionalClick = {
                viewModel.sent(WordFilterAction.Clear)
            }
        )

        val columns = if (isNotPhoneFormat()) 2 else 1


        Column(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                // 1. Original input
                item {
                    TextInput(label = "Original", value = state.original) {
                        viewModel.sent(WordFilterAction.SetOriginal(it))
                    }
                }

                // 2. Single select originalLang with ability to clear
                item {
                    SingleSelectInput(
                        value = state.originalLang,
                        items = Language.entries.filter { it != Language.UNDEFINED },
                        label = "Original language",
                        toLabel = { it.titleCase },
                        showNone = true,
                        noneLabel = "",
                        onSelect = { viewModel.sent(WordFilterAction.SetOriginalLang(it)) }
                    )
                }

                // 3. Translate input
                item {
                    TextInput(label = "Translate", value = state.translate) {
                        viewModel.sent(WordFilterAction.SetTranslate(it))
                    }
                }

                // 4. Single select translateLang with ability to clear
                item {
                    SingleSelectInput(
                        value = state.translateLang,
                        items = Language.entries.filter { it != Language.UNDEFINED },
                        label = "Translate language",
                        toLabel = { it.titleCase },
                        showNone = true,
                        noneLabel = "",
                        onSelect = { viewModel.sent(WordFilterAction.SetTranslateLang(it)) }
                    )
                }

                // 5. Multi select CEFR using generic MultiSelect component
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MultiSelect(
                            items = CEFR.entries.toList(),
                            selected = state.cefrs,
                            toLabel = { it.name },
                            onToggle = { level -> viewModel.sent(WordFilterAction.SetCefr(level)) },
                            label = "CEFR levels",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 6. Sort selector and asc toggle in one row
                item {
                    // Use the new generic SortSelector: provide items, selected, toLabel and handlers
                    SortSelector(
                        items = WordSortBy.entries.toList(),
                        selected = state.sortBy,
                        toLabel = { it.titleCase },
                        showDefault = false,
                        label = "Sort by",
                        expanded = sortByExpanded,
                        onExpandedChange = { sortByExpanded = it },
                        onSelect = { selectedSort ->
                            viewModel.sent(
                                WordFilterAction.SetSortBy(
                                    selectedSort
                                )
                            )
                        },
                        asc = state.asc,
                        onToggleAsc = { viewModel.sent(WordFilterAction.SetAsc(it)) },
                        expansionMode = vm.words.ua.words.ui.components.ExpansionMode.Dropdown
                    )
                }

                // 7. Categories input + list of added items with remove
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StringListInput(
                            label = "Add category",
                            items = state.categories,
                            onItemsChange = { viewModel.sent(WordFilterAction.SetCategories(it)) }
                        )
                    }
                }
            }

            // Apply button at the bottom
            Button(
                onClick = {
                    val filter = state.toWordFilter()
                    navController.popBackStack(
                        returnParam = WordFilterBundle(
                            filterId = bundle?.filterId ?: 0L,
                            filter = filter
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryColor,
                    contentColor = AppTheme.PrimaryBack
                )
            ) {
                Text("Apply Filter", fontSize = rememberFontSize())
            }
        }
    }
}
