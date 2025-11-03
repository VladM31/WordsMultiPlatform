package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiSelect(
    items: Collection<T>,
    selected: Collection<T>?,
    toLabel: (T) -> String,
    onToggle: (T) -> Unit,
    label: String = "Select",
    modifier: Modifier = Modifier
) {
    val selectedSet = selected?.toSet() ?: emptySet()
    val selectedText =
        if (selectedSet.isEmpty()) "Any" else selectedSet.joinToString(", ") { toLabel(it) }

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
            label = { Text(label, color = AppTheme.PrimaryGreen) },
            modifier = Modifier.fillMaxWidth(),
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
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggle(item) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AppTheme.PrimaryGreen,
                                    uncheckedColor = AppTheme.PrimaryGreen,
                                    checkmarkColor = AppTheme.PrimaryBack
                                )
                            )
                            Text(toLabel(item), color = AppTheme.PrimaryGreen)
                        }
                    },
                    onClick = { onToggle(item) }
                )
            }
        }
    }
}

