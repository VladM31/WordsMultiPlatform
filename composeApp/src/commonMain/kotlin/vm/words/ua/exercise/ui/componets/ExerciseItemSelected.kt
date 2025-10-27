package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.exercise.domain.models.data.ExerciseSelection

@Composable
fun ExerciseItemSelected(
    exerciseSelection: ExerciseSelection,
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B1B1B)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}