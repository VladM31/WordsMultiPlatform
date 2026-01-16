package vm.words.ua.learning.ui.screans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.LinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.di.rememberInstance
import vm.words.ua.learning.domain.models.enums.LearningHistoryType
import vm.words.ua.learning.ui.vms.StatisticLearningHistoryVm
import vm.words.ua.navigation.SimpleNavController

// Statistics data for chart
data class DayStatistics(
    val date: LocalDate,
    val learnedWords: Int,
    val reviewedWords: Int
)

@Composable
fun StatisticLearningHistoryScreen(
    navController: SimpleNavController,
    viewModel: StatisticLearningHistoryVm = rememberInstance()
) {
    val state by viewModel.state.collectAsState()

    // Convert StatisticsLearningHistory to DayStatistics grouped by date
    val dayStatistics = remember(state.statistic) {
        state.statistic
            .groupBy { it.date }
            .map { (date, items) ->
                DayStatistics(
                    date = date,
                    learnedWords = items.filter { it.type == LearningHistoryType.CREATE }.sumOf { it.count },
                    reviewedWords = items.filter { it.type == LearningHistoryType.UPDATE }.sumOf { it.count }
                )
            }
            .sortedBy { it.date }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppToolBar(
            title = "Learning Statistics",
            onBackClick = { navController.popBackStack() }
        )

        if (state.statistic.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
            }
        } else {
            StatisticsBarChart(
                statistics = dayStatistics,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun StatisticsBarChart(
    statistics: List<DayStatistics>,
    modifier: Modifier = Modifier
) {
    val learnedColor = Color(0xFF4CAF50)  // Green
    val reviewedColor = Color(0xFF2196F3) // Blue

    // Date formatting
    val dateFormatter = LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
    }

    // Prepare data
    val categories = statistics.map { dateFormatter.format(it.date) }
    val maxValue = statistics.maxOfOrNull { maxOf(it.learnedWords, it.reviewedWords) }?.toFloat() ?: 10f


    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
    ) {
        XYGraph(
            xAxisModel = CategoryAxisModel(categories),
            yAxisModel = LinearAxisModel(
                range = 0f..maxValue * 1.2f,
                minimumMajorTickIncrement = 1f
            ),
            xAxisLabels = { it },
            yAxisLabels = { it.toInt().toString() },
            xAxisTitle = null,
            yAxisTitle = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Learned words bars
            VerticalBarPlot(
                xData = categories,
                yData = statistics.map { it.learnedWords.toFloat() },
                bar = { _ ->
                    DefaultVerticalBar(
                        color = learnedColor
                    )
                },
                barWidth = 0.35f
            )

            // Reviewed words bars
            VerticalBarPlot(
                xData = categories,
                yData = statistics.map { it.reviewedWords.toFloat() },
                bar = { _ ->
                    DefaultVerticalBar(
                        color = reviewedColor
                    )
                },
                barWidth = 0.35f
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = learnedColor, label = "Learned")
            Spacer(modifier = Modifier.width(32.dp))
            LegendItem(color = reviewedColor, label = "Reviewed")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = AppTheme.PrimaryText
        )
    }
}