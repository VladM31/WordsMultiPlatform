package vm.words.ua.exercise.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.CenteredContainer
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.exercise.ui.bundles.ExerciseResultBundle
import vm.words.ua.exercise.ui.bundles.WordGradeResult
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.navigation.rememberParamOrThrow

@Composable
fun ExerciseResultScreen(navController: SimpleNavController) {
    val bundle = navController.rememberParamOrThrow<ExerciseResultBundle>()
    val fontSize = rememberFontSize()

    val resultsByExercise = remember(bundle) {
        bundle.wordResults.groupBy { it.exerciseId }
    }
    val exerciseById = remember(bundle) {
        bundle.exercises.associateBy { it.id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Results",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        CenteredContainer(maxWidth = rememberInterfaceMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    SummaryCard(bundle = bundle)
                }

                resultsByExercise.forEach { (exerciseId, results) ->
                    val exerciseName = exerciseById[exerciseId]?.text ?: "Exercise"
                    item {
                        Text(
                            text = exerciseName,
                            color = AppTheme.PrimaryColor,
                            fontSize = fontSize,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                        )
                    }
                    items(results) { result ->
                        WordResultRow(result = result)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(bundle: ExerciseResultBundle) {
    val labelFontSize = rememberLabelFontSize()
    val fontSize = rememberFontSize()
    val scaleFactor = rememberScaleFactor()

    val accuracy = bundle.accuracy
    val color = when {
        accuracy >= 80 -> AppTheme.PrimaryGreen
        accuracy >= 50 -> AppTheme.PrimaryYellow
        else -> AppTheme.ColorScheme.error
    }

    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animationStarted = true }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) accuracy / 100f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "accuracy_progress"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.SecondaryBack,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Trophy icon
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = "Trophy",
                tint = color,
                modifier = Modifier.size((52 * scaleFactor).dp)
            )

            // Circular progress
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size((115 * scaleFactor).dp),
                    color = color,
                    trackColor = AppTheme.ColorScheme.outline.copy(alpha = 0.3f),
                    strokeWidth = (8 * scaleFactor).dp,
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    text = "$accuracy%",
                    color = color,
                    fontSize = fontSize * 1.4f,
                    fontWeight = FontWeight.Bold
                )
            }

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "Total", value = bundle.totalWords.toString(), color = AppTheme.PrimaryText)
                StatItem(label = "Correct", value = bundle.correctWords.toString(), color = Color(0xFF4BC150))
                StatItem(
                    label = "Wrong",
                    value = (bundle.totalWords - bundle.correctWords).toString(),
                    color = AppTheme.ColorScheme.error
                )
            }

            // Exercises completed
            if (bundle.exercises.isEmpty()) {
                return@Column
            }
            Text(
                text = bundle.exercises.joinToString(" → ") { it.text },
                color = AppTheme.SecondaryText,
                fontSize = labelFontSize * 0.9f,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    val labelFontSize = rememberLabelFontSize()
    val fontSize = rememberFontSize()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = fontSize * 1.3f, fontWeight = FontWeight.Bold)
        Text(text = label, color = AppTheme.SecondaryText, fontSize = labelFontSize * 0.85f)
    }
}

@Composable
private fun WordResultRow(result: WordGradeResult) {
    val labelFontSize = rememberLabelFontSize()
    val fontSize = rememberFontSize()
    val iconColor = if (result.isCorrect) Color(0xFF4BC150) else AppTheme.ColorScheme.error

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AppTheme.SecondaryBack,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (result.isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.original,
                    color = AppTheme.PrimaryText,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = result.translate,
                    color = AppTheme.SecondaryText,
                    fontSize = labelFontSize * 0.9f,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            GradeStars(grade = result.grade, maxGrade = result.maxGrade)
        }
    }
}

@Composable
private fun GradeStars(grade: Int, maxGrade: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..maxGrade) {
            Icon(
                imageVector = if (i <= grade) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = if (i <= grade) Color(0xFFF2C144) else AppTheme.ColorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
