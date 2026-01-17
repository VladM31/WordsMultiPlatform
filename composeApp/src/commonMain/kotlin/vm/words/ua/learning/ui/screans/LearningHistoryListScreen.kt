package vm.words.ua.learning.ui.screans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.format.char
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.learning.domain.models.LearningHistory
import vm.words.ua.learning.domain.models.enums.LearningHistoryType
import vm.words.ua.learning.ui.actions.LearningHistoryListAction
import vm.words.ua.learning.ui.states.LearningHistoryListState
import vm.words.ua.learning.ui.vms.LearningHistoryListVm
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LearningHistoryListScreen(
    navController: SimpleNavController,
    viewModel: LearningHistoryListVm = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Load more when reaching end of list
    LaunchedEffect(listState.canScrollForward, state.isLoading) {
        if (!listState.canScrollForward && !state.isLoading && state.hasMore) {
            viewModel.sent(LearningHistoryListAction.LoadMore)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Learning History",
            onBackClick = { navController.popBackStack() }
        )

        LearningHistoryList(
            state = state,
            listState = listState
        )
    }
}

@Composable
private fun ColumnScope.LearningHistoryList(
    state: LearningHistoryListState,
    listState: LazyListState
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        if (state.history.isEmpty() && state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppTheme.PrimaryColor
            )
            return
        }

        if (state.history.isEmpty() && !state.isLoading) {
            Text(
                text = "No learning history found",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                color = AppTheme.SecondaryText
            )
            return
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = state.history.size,
                key = { index -> state.history[index].id }
            ) { index ->
                LearningHistoryItem(history = state.history[index])
            }

            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AppTheme.PrimaryColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = "Error: $error",
                color = AppTheme.PrimaryRed,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun LearningHistoryItem(
    history: LearningHistory,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor()
        val titleSize = rememberFontSize()
        val detailsSize = (14 * scaleFactor).sp
        val cardPadding = (16 * scaleFactor).dp
        val horizontalPadding = (16 * scaleFactor).dp
        val verticalPadding = (8 * scaleFactor).dp
        val spacerHeight = (8 * scaleFactor).dp

        // Format date
        val dateFormatter = kotlinx.datetime.LocalDate.Format {
            dayOfMonth()
            char('.')
            monthNumber()
            char('.')
            year()
        }
        val dateText = dateFormatter.format(history.date)

        // Type color and text
        val typeText = when (history.type) {
            LearningHistoryType.CREATE -> "Added"
            LearningHistoryType.UPDATE -> "Reviewed"
        }
        val typeColor = when (history.type) {
            LearningHistoryType.CREATE -> AppTheme.SecondaryColor
            LearningHistoryType.UPDATE -> AppTheme.PrimaryColor
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.SecondaryBack
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = history.original,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.PrimaryText,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = typeColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = typeText,
                            fontSize = (12 * scaleFactor).sp,
                            color = typeColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacerHeight))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = dateText,
                        fontSize = detailsSize,
                        color = AppTheme.SecondaryText
                    )

                    Text(
                        text = "${history.nativeLang.titleCase} → ${history.learningLang.titleCase}",
                        fontSize = detailsSize,
                        color = AppTheme.SecondaryText
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Level: ${history.cefr.name}",
                        fontSize = detailsSize,
                        color = AppTheme.SecondaryText
                    )

                    if (history.grade > 0) {
                        Text(
                            text = "Grade: ${history.grade}",
                            fontSize = detailsSize,
                            color = AppTheme.PrimaryColor
                        )
                    }
                }
            }
        }
    }
}

