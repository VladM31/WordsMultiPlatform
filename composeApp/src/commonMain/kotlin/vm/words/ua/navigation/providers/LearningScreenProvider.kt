package vm.words.ua.navigation.providers

import androidx.compose.runtime.Composable
import vm.words.ua.learning.ui.screans.LearningPlanScreen
import vm.words.ua.learning.ui.screans.StatisticLearningHistoryScreen
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.ScreenProvider
import vm.words.ua.navigation.SimpleNavController

class LearningScreenProvider : ScreenProvider {
    @Composable
    override fun provide(
        route: String,
        navController: SimpleNavController
    ): Boolean {
        when (route) {
            Screen.LeaningPlan.route -> {
                LearningPlanScreen(navController = navController)
            }
            Screen.StatisticLearningHistory.route -> {
                StatisticLearningHistoryScreen(navController = navController)
            }

            else -> {
                return false
            }
        }
        return true
    }
}