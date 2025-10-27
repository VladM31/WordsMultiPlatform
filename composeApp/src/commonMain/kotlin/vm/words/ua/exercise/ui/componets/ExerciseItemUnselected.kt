package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.exercise.domain.models.data.ExerciseSelection

@Composable
fun ExerciseItemUnselected(
    exerciseSelection: ExerciseSelection,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, AppTheme.PrimaryColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Text(
            text = exerciseSelection.exercise.text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.PrimaryColor
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}