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


@Composable
actual inline fun <reified T : Any> rememberInstance(): T {
    if (!isViewModel(T::class)) {
        return remember {
            DiContainer.di.direct.instance<T>()
        }
    }

    val navController: SimpleNavController = DiContainer.di.direct.instance()
    @Suppress("UNCHECKED_CAST")
    return viewModel<ViewModel>(
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
}

@PublishedApi
internal fun <T : Any> isViewModel(kClass: KClass<T>): Boolean {
    return ViewModel::class.java.isAssignableFrom(kClass.java)
}

