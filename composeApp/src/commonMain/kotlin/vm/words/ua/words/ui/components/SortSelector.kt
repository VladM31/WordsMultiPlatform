package vm.words.ua.words.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.arrow

@OptIn(ExperimentalMaterial3Api::class)
enum class ExpansionMode { Dropdown, Dialog }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SortSelector(
    items: List<T>,
    selected: T?,
    toLabel: (T) -> String,
    label: String = "Sort by",
    showDefault: Boolean = true,
    expanded: Boolean? = null, // if null - internal state will be used
    onExpandedChange: ((Boolean) -> Unit)? = null,
    onSelect: (T?) -> Unit,
    asc: Boolean = true,
    onToggleAsc: (Boolean) -> Unit = {},
    expansionMode: ExpansionMode = ExpansionMode.Dropdown,
    modifier: Modifier = Modifier
) {
    val internalExpanded = remember { mutableStateOf(false) }
    val isExpanded = expanded ?: internalExpanded.value
    val setExpanded: (Boolean) -> Unit = { value ->
        if (onExpandedChange != null) onExpandedChange(value) else internalExpanded.value = value
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Box(Modifier.weight(1f)) {
            when (expansionMode) {
                ExpansionMode.Dropdown -> {
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = setExpanded
                    ) {
                        OutlinedTextField(
                            value = selected?.let { toLabel(it) } ?: "Default",
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(
                                    label,
                                    color = AppTheme.PrimaryGreen,
                                    fontSize = getLabelFontSize()
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = isExpanded
                                )
                            },
                            textStyle = TextStyle(fontSize = rememberFontSize()),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AppTheme.PrimaryGreen,
                                unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                                focusedTextColor = AppTheme.PrimaryGreen,
                                unfocusedTextColor = AppTheme.PrimaryGreen,
                                cursorColor = AppTheme.PrimaryGreen
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { setExpanded(false) },
                            modifier = Modifier.background(AppTheme.PrimaryBack)
                        ) {
                            if (showDefault) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Default",
                                            color = AppTheme.PrimaryGreen,
                                            fontSize = getLabelFontSize()
                                        )
                                    },
                                    onClick = {
                                        onSelect(null)
                                        setExpanded(false)
                                    }
                                )
                            }

                            items.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            toLabel(item),
                                            color = AppTheme.PrimaryGreen,
                                            fontSize = getLabelFontSize()
                                        )
                                    },
                                    onClick = {
                                        onSelect(item)
                                        setExpanded(false)
                                    }
                                )
                            }
                        }
                    }
                }

                ExpansionMode.Dialog -> {
                    // Text field that opens a dialog with the list
                    OutlinedTextField(
                        value = selected?.let { toLabel(it) } ?: "Default",
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(
                                label,
                                color = AppTheme.PrimaryGreen,
                                fontSize = getLabelFontSize()
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = rememberFontSize()),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isExpanded
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

                    if (isExpanded) {
                        AlertDialog(
                            onDismissRequest = { setExpanded(false) },
                            title = {
                                Text(
                                    label,
                                    color = AppTheme.PrimaryGreen,
                                    fontSize = getLabelFontSize()
                                )
                            },
                            text = {
                                LazyColumn(modifier = Modifier.fillMaxHeight(0.6f)) {
                                    if (showDefault) item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onSelect(null)
                                                    setExpanded(false)
                                                }
                                                .padding(12.dp)) {
                                            Text(
                                                "Default",
                                                color = AppTheme.PrimaryGreen,
                                                fontSize = rememberFontSize()
                                            )
                                        }
                                    }

                                    items(items) { item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onSelect(item)
                                                    setExpanded(false)
                                                }
                                                .padding(12.dp)) {
                                            Text(
                                                toLabel(item),
                                                color = AppTheme.PrimaryGreen,
                                                fontSize = rememberFontSize()
                                            )
                                        }
                                    }
                                }
                            },
                            confirmButton = {},
                            dismissButton = {}
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { onToggleAsc(!asc) },
            modifier = Modifier.size(rememberIconSize())
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow),
                contentDescription = if (asc) "Ascending" else "Descending",
                tint = AppTheme.PrimaryGreen,
                modifier = Modifier.rotate(if (asc) -90f else 90f)
            )
        }
    }
}
