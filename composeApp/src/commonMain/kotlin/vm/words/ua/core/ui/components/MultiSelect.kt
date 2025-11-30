package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.core.utils.rememberFontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiSelect(
    items: Collection<T>,
    selected: Collection<T>?,
    toLabel: (T) -> String,
    onToggle: (T) -> Unit,
    label: String = "Select",
    emptyText: String = "",
    modifier: Modifier = Modifier
) {
    val selectedSet = selected?.toSet() ?: emptySet()
    val selectedText =
        if (selectedSet.isEmpty()) emptyText else selectedSet.joinToString(", ") { toLabel(it) }
    val fontSize = rememberFontSize()

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = AppTheme.PrimaryGreen, fontSize = getLabelFontSize()) },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
            textStyle = TextStyle(fontSize = fontSize),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.PrimaryGreen,
                unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                focusedTextColor = AppTheme.PrimaryGreen,
                unfocusedTextColor = AppTheme.PrimaryGreen,
                cursorColor = AppTheme.PrimaryGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(AppTheme.PrimaryBack)
        ) {
            items.forEach { item ->
                val isChecked = selectedSet.contains(item)
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggle(item) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AppTheme.PrimaryGreen,
                                    uncheckedColor = AppTheme.PrimaryGreen,
                                    checkmarkColor = AppTheme.PrimaryBack
                                )
                            )
                            Text(toLabel(item), color = AppTheme.PrimaryGreen, fontSize = fontSize)
                        }
                    },
                    onClick = { onToggle(item) }
                )
            }
        }
    }
}

