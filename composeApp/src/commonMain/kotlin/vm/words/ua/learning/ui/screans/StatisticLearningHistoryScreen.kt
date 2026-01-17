package vm.words.ua.learning.ui.screans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getIconButtonSize
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.learning.domain.models.enums.LearningHistoryType
import vm.words.ua.learning.ui.actions.StatisticLearningHistoryAction
import vm.words.ua.learning.ui.vms.StatisticLearningHistoryVm
import vm.words.ua.navigation.SimpleNavController

// Statistics data for chart
data class DayStatistics(
    val date: LocalDate,
    val learnedWords: Int,
    val reviewedWords: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticLearningHistoryScreen(
    navController: SimpleNavController,
    viewModel: StatisticLearningHistoryVm = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

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


    DatePickerComponent(
        showDatePicker = showDatePicker,
        onHide = { showDatePicker = false },
        viewModel = viewModel
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppToolBar(
            title = "Learning Statistics",
            onBackClick = { navController.popBackStack() }
        )


        // Current date range display
        val currentDateFormatted = remember(state.toDate) {
            val localDate = state.toDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
            "${localDate.dayOfMonth}.${localDate.monthNumber}.${localDate.year}"
        }




        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
            }
            return@Column
        }

        // Action buttons row
        Menu {
            showDatePicker = true
        }

        Text(
            text = "To: $currentDateFormatted",
            color = AppTheme.SecondaryText,
            fontSize = rememberLabelFontSize(),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        StatisticsBarChart(
            statistics = dayStatistics,
            toDate = state.toDate,
            step = state.step,
            modifier = Modifier.fillMaxWidth()
        )



    }
}

@Composable
private fun Menu(onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date picker button
        IconButton(
            onClick = onSelect
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "Select date",
                tint = AppTheme.PrimaryColor,
                modifier = Modifier.size(getIconButtonSize())
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // History list button (for future use)
        IconButton(
            onClick = {
                // TODO: Navigate to full history list
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.List,
                contentDescription = "View history",
                tint = AppTheme.PrimaryColor,
                modifier = Modifier.size(getIconButtonSize())
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerComponent(showDatePicker: Boolean, onHide: () -> Unit, viewModel: StatisticLearningHistoryVm) {
    val state by viewModel.state.collectAsState()

    // DatePicker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.toDate.toEpochMilliseconds()
    )

    if (!showDatePicker) {
        return
    }
    // DatePicker Dialog
    DatePickerDialog(
        onDismissRequest = onHide,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedInstant = Instant.fromEpochMilliseconds(millis)
                        viewModel.sent(StatisticLearningHistoryAction.SetDate(selectedInstant))
                    }
                    onHide()
                }
            ) {
                Text("OK", color = AppTheme.PrimaryColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onHide) {
                Text("Cancel", color = AppTheme.SecondaryText)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = AppTheme.PrimaryBack
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = AppTheme.PrimaryBack,
                titleContentColor = AppTheme.PrimaryText,
                headlineContentColor = AppTheme.PrimaryText,
                weekdayContentColor = AppTheme.SecondaryText,
                dayContentColor = AppTheme.PrimaryText,
                selectedDayContainerColor = AppTheme.PrimaryColor,
                selectedDayContentColor = AppTheme.PrimaryBack,
                todayContentColor = AppTheme.PrimaryColor,
                todayDateBorderColor = AppTheme.PrimaryColor
            )
        )
    }

}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun StatisticsBarChart(
    statistics: List<DayStatistics>,
    toDate: Instant,
    step: Int,
    modifier: Modifier = Modifier
) {
    val learnedColor = AppTheme.SecondaryColor
    val reviewedColor = AppTheme.PrimaryColor

    // Date formatting
    val dateFormatter = LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
    }

    // Generate all dates in range (from toDate - (step-1) to toDate) = exactly 'step' days
    val filledStatistics = remember(statistics, toDate, step) {
        val endDate = toDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = endDate.minus(DatePeriod(days = step - 1))

        val statsMap = statistics.associateBy { it.date }
        val result = mutableListOf<DayStatistics>()

        var currentDate = startDate
        while (currentDate <= endDate) {
            result.add(
                statsMap[currentDate] ?: DayStatistics(
                    date = currentDate,
                    learnedWords = 0,
                    reviewedWords = 0
                )
            )
            currentDate = currentDate.plus(DatePeriod(days = 1))
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
                    val barColor = colors.getOrElse(index) { learnedColor }
                    DefaultVerticalBar(
                        brush = SolidColor(barColor)
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