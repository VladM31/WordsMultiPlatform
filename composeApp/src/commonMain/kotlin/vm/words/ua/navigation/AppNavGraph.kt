package vm.words.ua.navigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.kodein.di.instance
import vm.words.ua.core.ui.screen.UpdateScreen
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.providers.*
import vm.words.ua.utils.net.hasInternet
import vm.words.ua.utils.net.ui.components.NoInternetBanner

@Composable
fun AppNavGraph() {
    val navController: SimpleNavController by DiContainer.di.instance()

    // List of providers - modules can add their own provider to this list
    val providers: List<ScreenProvider> = listOf(
        CoreScreenProvider(),
        AuthScreenProvider(),
        MainScreenProvider(),
        WordsScreenProvider(),
        PlayListScreenProvider(),
        ExerciseScreenProvider(),
        SettingScreenProvider()
    )

    val route = navController.currentRoute
    val isOnline = hasInternet()

    if (isOnline.not()) {
        NoInternetBanner()
        return
    }



    BackHandler(navController = navController)
    // First handle global special case
    if (route == Screen.UpdateApp.route) {
        UpdateScreen(navController = navController)
        return
    }

    // Delegate to providers. The first provider that returns true handles the route.
    val handled = providers.any { provider ->
        provider.provide(route, navController)
    }

    if (!handled) {
        Button(onClick = { navController.popBackStack() }) {
            Text("Unknown Screen, " + navController.currentRoute)
        }
    }
}
