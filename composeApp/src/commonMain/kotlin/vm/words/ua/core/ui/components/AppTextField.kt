@file:Suppress("unused")

package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.core.utils.rememberFontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    boxMaxWidth: Dp = Dp.Unspecified,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.padding(PaddingValues(top = 5.dp)),
    label: String? = null,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    // Optional helper text shown below the input
    helperText: String? = null
) {
    BoxWithConstraints(modifier.padding(top = 3.dp)) {
        val primaryColor70 = AppTheme.PrimaryColor.copy(alpha = 0.7f)

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = if (label != null) {
                { Text(label, color = primaryColor70, fontSize = getLabelFontSize()) }
            } else null,
            singleLine = singleLine,
            textStyle = androidx.compose.material3.LocalTextStyle.current.copy(fontSize = rememberFontSize()),
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
            ),
            supportingText = if (helperText != null) {
                { Text(helperText, color = primaryColor70, fontSize = getLabelFontSize() * 0.65) }
            } else null
        )
    }
}
