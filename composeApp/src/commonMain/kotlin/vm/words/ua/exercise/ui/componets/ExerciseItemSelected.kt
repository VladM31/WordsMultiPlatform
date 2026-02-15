package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.exercise.domain.models.data.ExerciseSelection

@Composable
fun ExerciseItemSelected(
    exerciseSelection: ExerciseSelection,
    fontSize: TextUnit,
    onRemove: (ExerciseSelection) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryColor, RoundedCornerShape(12.dp))
            .clickable { onRemove(exerciseSelection) }
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "${exerciseSelection.number}. ${exerciseSelection.exercise.text}",
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = AppTheme.PrimaryBack
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}