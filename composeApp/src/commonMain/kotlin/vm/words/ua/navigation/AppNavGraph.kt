package vm.words.ua.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.kodein.di.instance
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.core.ui.screen.UpdateScreen
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.providers.*
import vm.words.ua.utils.net.hasInternet
import vm.words.ua.utils.net.ui.components.NoInternetBanner

private val noInternetSupportRoutes = setOf(
    Screen.Home,
    Screen.UserWords,
    Screen.UserWordsFilter,
    Screen.Settings,
    Screen.PlayList,
    Screen.PlayListFilter
).map { it.route }

private val noHomeBtmSupportRoutes = setOf(
    Screen.UpdateApp,
    Screen.Login,
    Screen.SignUp,
    Screen.TelegramLogin,
    Screen.ConfirmSignUp
).map { it.route }

@Composable
fun AppNavGraph() {
    val navController: SimpleNavController by DiContainer.di.instance()
    val analytics by DiContainer.di.instance<Analytics>()

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
    val platform = currentPlatform()
    val showHomeBtn = remember(isOnline, route) {
        platform != AppPlatform.JS && noHomeBtmSupportRoutes.contains(route).not()
    }

    LaunchedEffect(route) {
        analytics.setCurrentScreen(route)
    }

    if (isOnline.not() && (noInternetSupportRoutes.contains(route).not() || platform == AppPlatform.JS)) {
        NoInternetBanner(
            navController = navController,
            showHomeBtn = showHomeBtn
        )
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
        LoaderScreen {

        }
    }
}
