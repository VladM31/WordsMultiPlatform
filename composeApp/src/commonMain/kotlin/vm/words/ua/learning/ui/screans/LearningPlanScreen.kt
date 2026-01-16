package vm.words.ua.learning.ui.screans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.di.rememberInstance
import vm.words.ua.learning.ui.vms.LearningPlanVm
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LearningPlanScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    viewModel: LearningPlanVm = rememberInstance()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
            .padding(top = 70.dp)
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Words learned today
        Text(
            text = "Words learned today",
            color = AppTheme.PrimaryColor,
            fontSize = 20.sp
        )

        Text(
            text = state.learnedWordsToDay.toString(),
            color = AppTheme.PrimaryColor,
            fontSize = 36.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // PieChart (через AndroidView, так как MPAndroidChart не имеет Compose версии)
//        AndroidView(
//            factory = { context ->
//                PieChart(context).apply {
//                    onSetupPieChart(this)
//                }
//            },
//            modifier = Modifier.size(width = 200.dp, height = 230.dp)
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))

        // Words count labels
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Added words",
                color = AppTheme.PrimaryColor,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Learned words",
                color = AppTheme.PrimaryColor,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        // Words count values
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = state.addedWords.toString(),
                color = AppTheme.PrimaryColor,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = state.learnedWords.toString(),
                color = AppTheme.PrimaryColor,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // CEFR level
        Text(
            text = "CEFR level",
            color = AppTheme.PrimaryColor,
            fontSize = 20.sp
        )


        Text(
            text = state.learningPlan?.cefr?.name.orEmpty(),
            color = AppTheme.PrimaryColor,
            fontSize = 36.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Language labels
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Native language",
                color = AppTheme.PrimaryColor,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Learning language",
                color = AppTheme.PrimaryColor,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }


        // Language values
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = state.learningPlan?.nativeLang?.name.orEmpty(),
                color = AppTheme.PrimaryColor,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = state.learningPlan?.learningLang?.name.orEmpty(),
                color = AppTheme.PrimaryColor,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}