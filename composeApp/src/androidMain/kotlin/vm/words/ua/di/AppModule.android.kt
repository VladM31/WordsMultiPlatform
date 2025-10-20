package vm.words.ua.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kodein.di.direct
import org.kodein.di.instance

/**
 * Android-specific implementation using AndroidX ViewModel
 * This survives configuration changes like screen rotation
 */
@Composable
actual inline fun <reified T : Any> rememberInstance(): T {
    return if (ViewModel::class.java.isAssignableFrom(T::class.java)) {
        // For ViewModel instances, use androidx viewModel() to survive config changes
        viewModel(
            modelClass = T::class.java as Class<ViewModel>,
            factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                    return DiContainer.di.direct.instance<T>() as VM
                }
            }
        ) as T
    } else {
        // For non-ViewModel instances, use regular DI
        androidx.compose.runtime.remember {
            DiContainer.di.direct.instance<T>()
        }
    }
}
