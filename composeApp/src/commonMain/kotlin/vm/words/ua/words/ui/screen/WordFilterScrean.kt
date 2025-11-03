package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.TextInput
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.mappers.toWordFilter
import vm.words.ua.words.domain.models.enums.WordSortBy
import vm.words.ua.words.domain.models.filters.WordFilter
import vm.words.ua.words.ui.actions.WordFilterAction
import vm.words.ua.words.ui.vms.WordFilterViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.add
import wordsmultiplatform.composeapp.generated.resources.arrow
import wordsmultiplatform.composeapp.generated.resources.delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordFilterScreen(
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
    var cefrExpanded by remember { mutableStateOf(false) }
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
                    TextInput(label = "Original", value = state.original) {
                        viewModel.sent(WordFilterAction.SetOriginal(it))
                    }
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
                    TextInput(label = "Translate", value = state.translate) {
                        viewModel.sent(WordFilterAction.SetTranslate(it))
                    }
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

                // 5. Multi select CEFRs with proper multi-selector
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val selected = state.cefrs?.toSet() ?: emptySet()
                        val selectedText = if (selected.isEmpty()) {
                            "Any"
                        } else {
                            selected.joinToString(", ") { it.name }
                        }

                        ExposedDropdownMenuBox(
                            expanded = cefrExpanded,
                            onExpandedChange = { cefrExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedText,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("CEFR levels", color = AppTheme.PrimaryGreen) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cefrExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AppTheme.PrimaryGreen,
                                    unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                                    focusedTextColor = AppTheme.PrimaryGreen,
                                    unfocusedTextColor = AppTheme.PrimaryGreen,
                                    cursorColor = AppTheme.PrimaryGreen
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = cefrExpanded,
                                onDismissRequest = { cefrExpanded = false },
                                modifier = Modifier.background(AppTheme.PrimaryBack)
                            ) {
                                CEFR.entries.forEach { level ->
                                    val isChecked = selected.contains(level)
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Checkbox(
                                                    checked = isChecked,
                                                    onCheckedChange = {
                                                        viewModel.sent(
                                                            WordFilterAction.SetCefr(
                                                                level
                                                            )
                                                        )
                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        checkedColor = AppTheme.PrimaryGreen,
                                                        uncheckedColor = AppTheme.PrimaryGreen,
                                                        checkmarkColor = AppTheme.PrimaryBack
                                                    )
                                                )
                                                Text(level.name, color = AppTheme.PrimaryGreen)
                                            }
                                        },
                                        onClick = { viewModel.sent(WordFilterAction.SetCefr(level)) }
                                    )
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

                        IconButton(
                            onClick = { viewModel.sent(WordFilterAction.SetAsc(!state.asc)) }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.arrow),
                                contentDescription = if (state.asc) "Ascending" else "Descending",
                                tint = AppTheme.PrimaryGreen,
                                modifier = Modifier.rotate(if (state.asc) -90f else 90f)
                            )
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
                                onValueChange = {
                                    val newText = it.text.take(255)
                                    val newSelectionEnd = minOf(it.selection.end, newText.length)
                                    val newSelectionStart =
                                        minOf(it.selection.start, newText.length)
                                    categoryInput = TextFieldValue(
                                        text = newText,
                                        selection = TextRange(newSelectionStart, newSelectionEnd)
                                    )
                                },
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
                            IconButton(
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
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.add),
                                    contentDescription = "Add",
                                    tint = AppTheme.PrimaryGreen
                                )
                            }
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


