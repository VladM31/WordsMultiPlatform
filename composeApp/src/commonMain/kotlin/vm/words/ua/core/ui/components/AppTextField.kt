@file:Suppress("unused")

package vm.words.ua.core.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import vm.words.ua.core.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    singleLine: Boolean = true,
    isPassword: Boolean = false
) {
    val primaryColor70 = AppTheme.PrimaryColor.copy(alpha = 0.7f)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = if (label != null) {
            { Text(label, color = primaryColor70) }
        } else null,
        singleLine = singleLine,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppTheme.PrimaryColor,
            unfocusedTextColor = AppTheme.PrimaryColor,
            focusedBorderColor = AppTheme.PrimaryColor,
            unfocusedBorderColor = primaryColor70,
            focusedLabelColor = AppTheme.PrimaryColor,
            unfocusedLabelColor = AppTheme.PrimaryColor,
            cursorColor = AppTheme.PrimaryColor,
            focusedPlaceholderColor = AppTheme.PrimaryColor,
            unfocusedPlaceholderColor = primaryColor70
        )
    )
}
