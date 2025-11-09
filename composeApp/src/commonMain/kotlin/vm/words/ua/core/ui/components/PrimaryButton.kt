@file:Suppress("unused")

package vm.words.ua.core.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getScaleFactor

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    val scaleFactor = getScaleFactor()
    val fontSize = getFontSize(0.75f * scaleFactor)

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.PrimaryColor,
            contentColor = AppTheme.PrimaryBack
        )
    ) {
        Text(text, fontSize = fontSize)
    }
}

