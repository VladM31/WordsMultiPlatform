package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    toKey: (List<T>, Int) -> Any,
    toItem: @Composable (index: Int, item: T) -> Unit,
    isLoading: Boolean,
    listState: LazyListState
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        if (isLoading) {
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
                key = { index -> toKey(content, index) }
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