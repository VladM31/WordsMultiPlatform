package vm.words.ua.core.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.states.ToastPosition
import vm.words.ua.core.ui.states.ToastState
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize

@Composable
fun rememberToast(): ToastState = remember { ToastState() }

@Composable
fun AppToast(
    toastState: ToastState,
    modifier: Modifier = Modifier
) {
    val toast = toastState.currentToast

    LaunchedEffect(toast) {
        if (toast != null) {
            delay(toast.duration)
            toastState.dismiss()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = when (toast?.position) {
            ToastPosition.Top -> Alignment.TopCenter
            ToastPosition.Center -> Alignment.Center
            ToastPosition.Bottom, null -> Alignment.BottomCenter
        }
    ) {
        AnimatedVisibility(
            visible = toast != null,
            enter = slideInVertically(
                initialOffsetY = { if (toast?.position == ToastPosition.Top) -it else it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { if (toast?.position == ToastPosition.Top) -it else it }
            ) + fadeOut()
        ) {
            val currentToast = toast ?: return@AnimatedVisibility
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 400.dp),
                shape = RoundedCornerShape(8.dp),
                color = AppTheme.SecondaryBack,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentToast.message,
                        color = AppTheme.SecondaryText,
                        modifier = Modifier.weight(1f),
                        fontSize = rememberLabelFontSize(),
                        lineHeight = rememberLabelFontSize() * 1.1,
                        textAlign = TextAlign.Center
                    )

                    val hasNotBtn = currentToast.buttonText == null && currentToast.onButtonClick == null
                    if (hasNotBtn) {
                        return@Row
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            currentToast.onButtonClick?.invoke()
                            toastState.dismiss()
                        }
                    ) {
                        Text(
                            text = currentToast.buttonText.orEmpty(),
                            color = AppTheme.PrimaryColor,
                            fontSize = rememberFontSize() * 0.9
                        )
                    }
                }
            }
        }
    }
}