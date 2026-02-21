@file:Suppress("unused")

package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberButtonMaxWidth
import vm.words.ua.core.utils.rememberFontSize

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.padding(6.dp),
    enabled: Boolean = true
) {

    val fontSize = rememberFontSize()

    Button(
        onClick = onClick,
        modifier = modifier
            .widthIn(max = rememberButtonMaxWidth())
            .fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.PrimaryColor,
            contentColor = AppTheme.PrimaryBack
        )
    ) {
        Text(text, fontSize = fontSize, textAlign = TextAlign.Center)
    }
}

