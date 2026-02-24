package vm.words.ua.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

val LocalSwipeRightOverride = compositionLocalOf<MutableState<(() -> Unit)?>> {
    mutableStateOf(null)
}

val LocalSwipeLeftOverride = compositionLocalOf<MutableState<(() -> Unit)?>> {
    mutableStateOf(null)
}

val LocalSwipeUpOverride = compositionLocalOf<MutableState<(() -> Unit)?>> {
    mutableStateOf(null)
}

val LocalSwipeDownOverride = compositionLocalOf<MutableState<(() -> Unit)?>> {
    mutableStateOf(null)
}

