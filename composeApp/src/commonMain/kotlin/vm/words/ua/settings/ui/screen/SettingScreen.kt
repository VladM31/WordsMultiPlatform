package vm.words.ua.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.kodein.di.instance
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun SettingScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val textSize = remember { 40.sp }
    val scope = rememberCoroutineScope()
    val userCacheManager: UserCacheManager by DiContainer.di.instance()

    val buttons = remember {
        listOf(
            GridButtonItem("Subscription"),
            GridButtonItem("History"),
            GridButtonItem("Plan"),
            GridButtonItem("Profile"),
            GridButtonItem("Policy"),
            GridButtonItem("Log Out") {
                scope.launch {
                    userCacheManager.clear() // await completion
                    navController.navigateAndClear(Screen.Login)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(title = "Settings", showBackButton = false)

        ButtonsGrid(
            items = buttons,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxButtonWidth = 300.dp,
            textSizeSp = textSize.value
        )

        BottomNavBar(currentRoute = Screen.Home, onNavigate = { route -> navController.navigate(route) })
    }
}


