package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.ui.actions.PlayListDetailsAction
import vm.words.ua.playlist.ui.components.WordItem
import vm.words.ua.playlist.ui.vms.PlayListDetailsViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete_red
import wordsmultiplatform.composeapp.generated.resources.play
import wordsmultiplatform.composeapp.generated.resources.setting

@Composable
fun PlayListDetailsScreen(
    playListId: String,
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    // Fetch playlist on first composition
    LaunchedEffect(playListId) {
        viewModel.sent(PlayListDetailsAction.Fetch(playListId))
    }

    // Handle navigation back when playlist is deleted
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = state.name,
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = true,
            additionalButtonImage = painterResource(Res.drawable.setting),
            onAdditionalClick = {
                // TODO: Show context menu with Edit and Remove options
                // For now, just show delete action
//                viewModel.sent(PlayListDetailsAction.Delete)
            }
        )

        // Words list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            itemsIndexed(
                items = state.words,
                key = { _, word -> word.userWord.id }
            ) { index, pinnedWord ->
                WordItem(
                    word = pinnedWord.userWord.word,
                    isSelected = state.selectedWords.containsKey(pinnedWord.userWord.id),
                    onSelect = {
                        viewModel.sent(
                            PlayListDetailsAction.SelectWord(
                                pinnedWord.userWord.id,
                                index
                            )
                        )
                    },
                    onOpen = {
                        // TODO: Navigate to word details
                    }
                )
            }
        }

        // Bottom control panel
        BottomControlPanel(
            selectedWordsCount = state.selectedWords.size,
            totalWordsCount = state.words.size,
            onUnselect = { viewModel.sent(PlayListDetailsAction.UnSelect) },
            onUnpin = { viewModel.sent(PlayListDetailsAction.UnPin) },
            onPlay = {
                // TODO: Start exercise transaction
            }
        )
    }
}

@Composable
private fun BottomControlPanel(
    selectedWordsCount: Int,
    totalWordsCount: Int,
    onUnselect: () -> Unit,
    onUnpin: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasSelected = selectedWordsCount > 0

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        color = AppTheme.PrimaryBack,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Two buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Unpin button
                IconButton(
                    onClick = onUnpin,
                    enabled = selectedWordsCount > 0,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.delete_red),
                        contentDescription = "Unpin selected words",
                        tint = if (selectedWordsCount > 0) Color.Unspecified else Color.Gray,
                        modifier = Modifier.size(60.dp)
                    )
                }

                // Unselect button
                Button(
                    onClick = onUnselect,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasSelected) AppTheme.PrimaryColor else AppTheme.PrimaryGray
                    ),
                    shape = CircleShape,
                    enabled = hasSelected
                ) {
                    Text(
                        text = "Unselect",
                        color = AppTheme.PrimaryBack,
                        fontSize = 24.sp
                    )
                }
            }

            // Center: Words count
            Text(
                text = if (selectedWordsCount == 0) "All" else "$selectedWordsCount",
                color = AppTheme.PrimaryColor,
                fontSize = 24.sp
            )

            // Right side: Play button
            IconButton(
                onClick = onPlay,
                enabled = totalWordsCount > 0,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.play),
                    contentDescription = "Start training",
                    tint = if (totalWordsCount > 0) Color.Unspecified else Color.Gray,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}
