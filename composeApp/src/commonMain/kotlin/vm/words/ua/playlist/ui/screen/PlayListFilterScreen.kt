package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.CenteredContainer
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.ui.actions.PlayListFilterAction
import vm.words.ua.playlist.ui.vms.PlayListFilterViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.find

@Composable
fun PlayListFilterScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListFilterViewModel>()
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Get current filter from navigation params
    val currentFilter = navController.getParam<PlayListCountFilter>() ?: PlayListCountFilter()

    // Initialize with current filter
    LaunchedEffect(Unit) {
        viewModel.send(PlayListFilterAction.Init(currentFilter))
    }

    // Navigate back when filter is applied
    LaunchedEffect(state.isEnd) {
        if (state.isEnd) {
            navController.popBackStack(returnParam = viewModel.toFilter())
        }
    }



    CenteredContainer(
        maxWidth = 600.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.PrimaryBack)
        ) {
            AppToolBar(
                title = "Playlist Filter",
                showBackButton = true
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Name input
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.send(PlayListFilterAction.ChangeName(it)) },
                    label = { Text("Playlist name", color = AppTheme.PrimaryGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppTheme.PrimaryGreen,
                        unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                        focusedTextColor = AppTheme.PrimaryGreen,
                        unfocusedTextColor = AppTheme.PrimaryGreen,
                        cursorColor = AppTheme.PrimaryGreen
                    ),
                    singleLine = true
                )

                // Count section
                Text(
                    text = "Count",
                    fontSize = 26.sp,
                    color = AppTheme.PrimaryGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Start and End count inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.startCount,
                        onValueChange = { viewModel.send(PlayListFilterAction.ChangeStartCount(it)) },
                        label = { Text("Start", color = AppTheme.PrimaryGreen) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 56.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.PrimaryGreen,
                            unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                            focusedTextColor = AppTheme.PrimaryGreen,
                            unfocusedTextColor = AppTheme.PrimaryGreen,
                            cursorColor = AppTheme.PrimaryGreen
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.endCount,
                        onValueChange = { viewModel.send(PlayListFilterAction.ChangeEndCount(it)) },
                        label = { Text("End", color = AppTheme.PrimaryGreen) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 56.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppTheme.PrimaryGreen,
                            unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                            focusedTextColor = AppTheme.PrimaryGreen,
                            unfocusedTextColor = AppTheme.PrimaryGreen,
                            cursorColor = AppTheme.PrimaryGreen
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            // Find button at the bottom
            Button(
                onClick = { viewModel.send(PlayListFilterAction.Find) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryGreen,
                    contentColor = AppTheme.PrimaryBack
                )
            ) {
                Text(
                    text = "Find",
                    fontSize = 30.sp,
                    color = AppTheme.PrimaryBack
                )
            }
        }
    }
}
