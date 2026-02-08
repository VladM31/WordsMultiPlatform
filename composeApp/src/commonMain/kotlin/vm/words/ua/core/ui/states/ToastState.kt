package vm.words.ua.core.ui.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class ToastData(
    val message: String,
    val buttonText: String? = null,
    val onButtonClick: (() -> Unit)? = null,
    val duration: Long = 3000L,
    val position: ToastPosition = ToastPosition.Bottom
)

enum class ToastPosition {
    Top, Center, Bottom
}

class ToastState {
    var currentToast by mutableStateOf<ToastData?>(null)
        private set

    fun show(toast: ToastData) {
        currentToast = toast
    }

    fun dismiss() {
        currentToast = null
    }
}