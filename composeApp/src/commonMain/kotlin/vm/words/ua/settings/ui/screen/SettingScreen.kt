package vm.words.ua.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.instance
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.core.ui.components.ButtonsGrid
import vm.words.ua.core.ui.components.GridButtonItem
import vm.words.ua.core.utils.appHeightDp
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberWidthDeviceFormat
import vm.words.ua.di.DiContainer
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
fun SettingScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val userCacheManager: UserCacheManager by DiContainer.di.instance()
    val isPhoneFormat = rememberWidthDeviceFormat().isPhone
    val isNotBigHeight = appHeightDp() < 500.dp
    val spacerHeight = remember(isPhoneFormat, isNotBigHeight) {
        if (isPhoneFormat && isNotBigHeight) 8.dp else 96.dp
    }


    val buttons = remember {
        listOf(
            GridButtonItem("History", icon = Icons.Outlined.Schedule) {
                navController.navigate(Screen.StatisticLearningHistory)
            },
            GridButtonItem("Plan", icon = Icons.Filled.DateRange) {
                navController.navigate(Screen.LeaningPlan)
            },
            GridButtonItem("Profile", icon = Icons.Filled.Person) {
                navController.navigate(Screen.Profile)
            },
            GridButtonItem("Theme", icon = Icons.Outlined.Palette) {
                navController.navigate(Screen.Theme)
            },
            GridButtonItem("Policy", icon = Icons.Outlined.PrivacyTip) {
                navController.navigate(Screen.Policy)
            },
            GridButtonItem("Log Out", icon = Icons.AutoMirrored.Filled.ExitToApp) {
                userCacheManager.clear()
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        Spacer(modifier = Modifier.height(spacerHeight))

        ButtonsGrid(
            items = buttons,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = "Version " + AppRemoteConfig.version,
            color = AppTheme.PrimaryDisable,
            fontSize = rememberFontSize() * 0.5,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        BottomNavBar(currentRoute = Screen.Settings, onNavigate = { route -> navController.navigateAndClear(route) })
    }
}


