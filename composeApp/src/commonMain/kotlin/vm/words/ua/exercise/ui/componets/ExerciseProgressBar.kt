package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.states.ExerciseState

@Composable
fun ExerciseProgressBar(
    state: ExerciseState,
    bundle: ExerciseBundle,
    modifier: Modifier = Modifier,
) {
    val totalWords = bundle.words.size
    if (totalWords == 0) return

    // wordIndex is the current word (0-based), completed = wordIndex
    val completedWords = state.wordIndex.coerceIn(0, totalWords)
    val wordProgress = completedWords.toFloat() / totalWords.toFloat()

    val totalExercises = bundle.exercises.count { it.isSelected }
    val currentExerciseNumber = bundle.number + 1 // 1-based
    val showExerciseProgress = totalExercises > 1

    val labelFontSize = rememberLabelFontSize()
    val maxWidth = rememberInterfaceMaxWidth()

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Words progress label
                Text(
                    text = "Word $completedWords / $totalWords",
                    color = AppTheme.SecondaryText,
                    fontSize = labelFontSize * 0.85f,
                    fontWeight = FontWeight.Medium
                )

                // Exercise progress label — only if multiple exercises
                if (showExerciseProgress) {
                    Text(
                        text = "Exercise $currentExerciseNumber / $totalExercises",
                        color = AppTheme.PrimaryColor,
                        fontSize = labelFontSize * 0.85f,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Word progress bar
            LinearProgressIndicator(
                progress = { wordProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AppTheme.PrimaryColor,
                trackColor = AppTheme.ColorScheme.outline.copy(alpha = 0.25f),
                strokeCap = StrokeCap.Round,
            )
        }
    }
}

