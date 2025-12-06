package vm.words.ua.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kodein.di.direct
import org.kodein.di.instance
import vm.words.ua.navigation.SimpleNavController
import kotlin.reflect.KClass

/**
 * JS/Web implementation using ViewModelStoreOwner and ViewModelProvider.Factory
 * Proper ViewModel lifecycle management per route
 */
@Composable
actual inline fun <reified T : Any> rememberInstance(): T {
    return if (isViewModel<T>()) {
        val navController: SimpleNavController = DiContainer.di.direct.instance()
        @Suppress("UNCHECKED_CAST")
        viewModel<ViewModel>(
            viewModelStoreOwner = navController.viewModelStoreOwner(),
            modelClass = T::class as KClass<ViewModel>,
            factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <VM : ViewModel> create(
                    modelClass: KClass<VM>,
                    extras: androidx.lifecycle.viewmodel.CreationExtras
                ): VM {
                    return DiContainer.di.direct.instance<T>() as VM
                }
            }
        ) as T
    } else {
        remember {
            DiContainer.di.direct.instance<T>()
        }
    }
}

@PublishedApi
internal inline fun <reified T : Any> isViewModel(): Boolean {
    // For JS, check simple name contains ViewModel pattern
    val simpleName = T::class.simpleName ?: return false
    return simpleName.endsWith("ViewModel") || simpleName == "ViewModel"
}

