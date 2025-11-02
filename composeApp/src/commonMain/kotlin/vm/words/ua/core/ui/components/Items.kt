package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme


@Composable
fun <T> ColumnScope.Items(
    content: List<T>,
    toKey: (Int) -> Any,
    toItem: @Composable (index: Int, item: T) -> Unit,
    isLoading: Boolean,
    listState: LazyListState
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        if (content.isEmpty() && isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppTheme.PrimaryGreen
            )
            return
        }

        if (content.isEmpty() && isLoading.not()) {
            Text(
                text = "No playlists found",
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
                count = content.size,
                key = toKey
            ) { index ->
                toItem(index, content[index])
            }

            if (isLoading.not()) {
                return@LazyColumn
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.PrimaryGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}