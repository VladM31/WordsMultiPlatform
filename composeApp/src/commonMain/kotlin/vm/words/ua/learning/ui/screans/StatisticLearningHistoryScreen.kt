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
import androidx.compose.ui.graphics.SolidColor
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

    // Fill missing dates between min and max date
    val filledStatistics = remember(statistics) {
        if (statistics.isEmpty()) return@remember emptyList()

        val sorted = statistics.sortedBy { it.date }
        val minDate = sorted.first().date
        val maxDate = sorted.last().date

        val statsMap = sorted.associateBy { it.date }
        val result = mutableListOf<DayStatistics>()

        var currentDate = minDate
        while (currentDate <= maxDate) {
            result.add(
                statsMap[currentDate] ?: DayStatistics(
                    date = currentDate,
                    learnedWords = 0,
                    reviewedWords = 0
                )
            )
            currentDate = LocalDate(
                currentDate.year,
                currentDate.monthNumber,
                currentDate.dayOfMonth
            ).let {
                // Add one day
                val epochDays = it.toEpochDays() + 1
                LocalDate.fromEpochDays(epochDays)
            }
        }
        result
    }

    // Create categories: each date has two sub-bars but one label
    // Use index-based categories for proper bar placement
    val dateLabels = filledStatistics.map { dateFormatter.format(it.date) }

    // Create interleaved categories for bars (2 per date)
    val barCategories = filledStatistics.flatMapIndexed { index, _ ->
        listOf("${index}_added", "${index}_reviewed")
    }

    val values = filledStatistics.flatMap { stat ->
        listOf(stat.learnedWords.toFloat(), stat.reviewedWords.toFloat())
    }

    val colors = filledStatistics.flatMap {
        listOf(learnedColor, reviewedColor)
    }

    val maxValue = (values.maxOrNull() ?: 10f).coerceAtLeast(1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
    ) {
        XYGraph(
            xAxisModel = CategoryAxisModel(barCategories),
            yAxisModel = LinearAxisModel(
                range = 0f..maxValue * 1.2f,
                minimumMajorTickIncrement = 1f
            ),
            xAxisLabels = { category ->
                // Show date label only for "added" bars (first of each pair)
                if (category.endsWith("_added")) {
                    val index = category.substringBefore("_").toIntOrNull() ?: 0
                    dateLabels.getOrElse(index) { "" }
                } else {
                    "" // Empty for "reviewed" bars
                }
            },
            yAxisLabels = { it.toInt().toString() },
            xAxisTitle = null,
            yAxisTitle = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            VerticalBarPlot(
                xData = barCategories,
                yData = values,
                bar = { index ->
                    DefaultVerticalBar(
                        brush = SolidColor(colors[index])
                    )
                },
                barWidth = 0.9f
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = learnedColor, label = "Added")
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