package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.mappers.toWordFilter
import vm.words.ua.words.domain.models.enums.WordSortBy
import vm.words.ua.words.domain.models.filters.WordFilter
import vm.words.ua.words.ui.actions.WordFilterAction
import vm.words.ua.words.ui.vms.WordFilterViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordFilterScrean(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<WordFilterViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Local UI states
    var originalLangExpanded by remember { mutableStateOf(false) }
    var translateLangExpanded by remember { mutableStateOf(false) }
    var sortByExpanded by remember { mutableStateOf(false) }
    var categoryInput by remember { mutableStateOf(TextFieldValue("")) }

    // Get current filter from navigation params
    val currentFilter = navController.getParam<WordFilter>() ?: WordFilter()

    // Initialize with current filter
    LaunchedEffect(Unit) {
        viewModel.sent(WordFilterAction.Init(currentFilter))
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
                // Clear all fields
                viewModel.sent(WordFilterAction.SetOriginal(null))
                viewModel.sent(WordFilterAction.SetOriginalLang(null))
                viewModel.sent(WordFilterAction.SetTranslate(null))
                viewModel.sent(WordFilterAction.SetTranslateLang(null))
                viewModel.sent(WordFilterAction.SetCategories(null))
                viewModel.sent(WordFilterAction.SetSortBy(null))
                viewModel.sent(WordFilterAction.SetAsc(true))
                viewModel.sent(WordFilterAction.SetCefrs(null))
                categoryInput = TextFieldValue("")
            }
        )

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Original input
                item {
                    OutlinedTextField(
                        value = state.original.orEmpty(),
                        onValueChange = { viewModel.sent(WordFilterAction.SetOriginal(it.ifBlank { null })) },
                        label = { Text("Original", color = AppTheme.PrimaryGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.PrimaryGreen,
                            unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                            focusedTextColor = AppTheme.PrimaryGreen,
                            unfocusedTextColor = AppTheme.PrimaryGreen,
                            cursorColor = AppTheme.PrimaryGreen
                        )
                    )
                }

                // 2. Single select originalLang with ability to clear
                item {
                    Box {
                        ExposedDropdownMenuBox(
                            expanded = originalLangExpanded,
                            onExpandedChange = { originalLangExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = state.originalLang?.titleCase ?: "None",
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(
                                        "Original language",
                                        color = AppTheme.PrimaryGreen
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originalLangExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.PrimaryGreen,
                                    unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                                    focusedTextColor = AppTheme.PrimaryGreen,
                                    unfocusedTextColor = AppTheme.PrimaryGreen,
                                    cursorColor = AppTheme.PrimaryGreen
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = originalLangExpanded,
                                onDismissRequest = { originalLangExpanded = false },
                                modifier = Modifier.background(AppTheme.PrimaryBack)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("None", color = AppTheme.PrimaryGreen) },
                                    onClick = {
                                        viewModel.sent(WordFilterAction.SetOriginalLang(null))
                                        originalLangExpanded = false
                                    }
                                )
                                Language.entries.filter { it != Language.UNDEFINED }
                                    .forEach { lang ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    lang.titleCase,
                                                    color = AppTheme.PrimaryGreen
                                                )
                                            },
                                            onClick = {
                                                viewModel.sent(WordFilterAction.SetOriginalLang(lang))
                                                originalLangExpanded = false
                                            }
                                        )
                                    }
                            }
                        }
                    }
                }

                // 3. Translate input
                item {
                    OutlinedTextField(
                        value = state.translate.orEmpty(),
                        onValueChange = { viewModel.sent(WordFilterAction.SetTranslate(it.ifBlank { null })) },
                        label = { Text("Translate", color = AppTheme.PrimaryGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.PrimaryGreen,
                            unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                            focusedTextColor = AppTheme.PrimaryGreen,
                            unfocusedTextColor = AppTheme.PrimaryGreen,
                            cursorColor = AppTheme.PrimaryGreen
                        )
                    )
                }

                // 4. Single select translateLang with ability to clear
                item {
                    Box {
                        ExposedDropdownMenuBox(
                            expanded = translateLangExpanded,
                            onExpandedChange = { translateLangExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = state.translateLang?.titleCase ?: "None",
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(
                                        "Translate language",
                                        color = AppTheme.PrimaryGreen
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = translateLangExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.PrimaryGreen,
                                    unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                                    focusedTextColor = AppTheme.PrimaryGreen,
                                    unfocusedTextColor = AppTheme.PrimaryGreen,
                                    cursorColor = AppTheme.PrimaryGreen
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = translateLangExpanded,
                                onDismissRequest = { translateLangExpanded = false },
                                modifier = Modifier.background(AppTheme.PrimaryBack)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("None", color = AppTheme.PrimaryGreen) },
                                    onClick = {
                                        viewModel.sent(WordFilterAction.SetTranslateLang(null))
                                        translateLangExpanded = false
                                    }
                                )
                                Language.entries.filter { it != Language.UNDEFINED }
                                    .forEach { lang ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    lang.titleCase,
                                                    color = AppTheme.PrimaryGreen
                                                )
                                            },
                                            onClick = {
                                                viewModel.sent(
                                                    WordFilterAction.SetTranslateLang(
                                                        lang
                                                    )
                                                )
                                                translateLangExpanded = false
                                            }
                                        )
                                    }
                            }
                        }
                    }
                }

                // 5. Multi select CEFRs with toggle and set-all/clear-all controls
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("CEFR", color = AppTheme.PrimaryGreen)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.sent(WordFilterAction.SetCefrs(CEFR.entries.toSet())) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppTheme.PrimaryGreen,
                                    contentColor = AppTheme.PrimaryBack
                                )
                            ) { Text("Select all") }
                            OutlinedButton(onClick = { viewModel.sent(WordFilterAction.SetCefrs(null)) }) {
                                Text(
                                    "Clear",
                                    color = AppTheme.PrimaryGreen
                                )
                            }
                        }
                        // Show CEFR options as toggles
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val selected = state.cefrs?.toSet() ?: emptySet()
                            CEFR.entries.chunked(3).forEach { rowItems ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowItems.forEach { level ->
                                        val isSelected = selected.contains(level)
                                        Button(
                                            onClick = {
                                                viewModel.sent(
                                                    WordFilterAction.SetCefr(
                                                        level
                                                    )
                                                )
                                            },
                                            colors = if (isSelected) {
                                                ButtonDefaults.buttonColors(
                                                    containerColor = AppTheme.PrimaryGreen,
                                                    contentColor = AppTheme.PrimaryBack
                                                )
                                            } else {
                                                ButtonDefaults.buttonColors(
                                                    containerColor = AppTheme.SecondaryBack,
                                                    contentColor = AppTheme.PrimaryGreen
                                                )
                                            }
                                        ) {
                                            Text(level.name)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 6. Sort selector and asc toggle in one row
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(Modifier.weight(1f)) {
                            ExposedDropdownMenuBox(
                                expanded = sortByExpanded,
                                onExpandedChange = { sortByExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = state.sortBy?.titleCase ?: "Default",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Sort by", color = AppTheme.PrimaryGreen) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = sortByExpanded
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AppTheme.PrimaryGreen,
                                        unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                                        focusedTextColor = AppTheme.PrimaryGreen,
                                        unfocusedTextColor = AppTheme.PrimaryGreen,
                                        cursorColor = AppTheme.PrimaryGreen
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = sortByExpanded,
                                    onDismissRequest = { sortByExpanded = false },
                                    modifier = Modifier.background(AppTheme.PrimaryBack)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Default", color = AppTheme.PrimaryGreen) },
                                        onClick = {
                                            viewModel.sent(WordFilterAction.SetSortBy(null))
                                            sortByExpanded = false
                                        }
                                    )
                                    WordSortBy.entries.forEach { sort ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    sort.titleCase,
                                                    color = AppTheme.PrimaryGreen
                                                )
                                            },
                                            onClick = {
                                                viewModel.sent(WordFilterAction.SetSortBy(sort))
                                                sortByExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.sent(WordFilterAction.SetAsc(!state.asc)) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.PrimaryGreen,
                                contentColor = AppTheme.PrimaryBack
                            )
                        ) {
                            Text(if (state.asc) "Asc" else "Desc")
                        }
                    }
                }

                // 7. Categories input + list of added items with remove
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = categoryInput,
                                onValueChange = { categoryInput = it },
                                label = { Text("Add category", color = AppTheme.PrimaryGreen) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.PrimaryGreen,
                                    unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                                    focusedTextColor = AppTheme.PrimaryGreen,
                                    unfocusedTextColor = AppTheme.PrimaryGreen,
                                    cursorColor = AppTheme.PrimaryGreen
                                )
                            )
                            Button(
                                onClick = {
                                    val value = categoryInput.text.trim()
                                    val current =
                                        state.categories?.toMutableList() ?: mutableListOf()
                                    if (value.isNotEmpty() && current.contains(value).not()) {
                                        current.add(value)
                                        viewModel.sent(WordFilterAction.SetCategories(current))
                                        categoryInput = TextFieldValue("")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppTheme.PrimaryGreen,
                                    contentColor = AppTheme.PrimaryBack
                                )
                            ) { Text("Add") }
                        }

                        val categories = state.categories ?: emptyList()
                        if (categories.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                categories.forEach { cat ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(cat, color = AppTheme.PrimaryGreen)
                                        OutlinedButton(
                                            onClick = {
                                                val updated = categories.toMutableList()
                                                    .also { it.remove(cat) }
                                                viewModel.sent(WordFilterAction.SetCategories(if (updated.isEmpty()) null else updated))
                                            }
                                        ) { Text("Remove", color = AppTheme.PrimaryGreen) }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Apply button at the bottom
            Button(
                onClick = {
                    val filter = state.toWordFilter()
                    navController.popBackStack(returnParam = filter)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryGreen,
                    contentColor = AppTheme.PrimaryBack
                )
            ) {
                Text("Apply Filter")
            }
        }
    }
}
