package vm.words.ua.navigation

import androidx.compose.runtime.Composable


interface ScreenProvider {

    @Composable
    fun provide(route: String, navController: SimpleNavController): Boolean
}

