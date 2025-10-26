package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.ImageFromBytes
import vm.words.ua.core.ui.components.PopupMenuButton
import vm.words.ua.core.ui.components.PopupMenuItem
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.ui.actions.WordDetailsAction
import vm.words.ua.words.ui.vms.WordDetailsViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete_red
import wordsmultiplatform.composeapp.generated.resources.image_icon
import wordsmultiplatform.composeapp.generated.resources.sound

@Composable
fun WordDetailsScreen(
    userWord: UserWord?,
    word: Word,
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<WordDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    val wordState = viewModel.state.map { it -> it.word }.collectAsState(null)
    val image by viewModel.state.map { it -> it.image }.collectAsState(null)

    // Fetch word on first composition
    LaunchedEffect(Unit) {
        viewModel.sent(WordDetailsAction.Init(userWord, word))
    }

    // Handle navigation back when word is deleted
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        Box {
            AppToolBar(
                title = wordState.value?.original ?: "Word Details",
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                showAdditionalButton = false
            )

            // Popup menu positioned at top right
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                PopupMenuButton(
                    items = listOf(
                        PopupMenuItem(
                            text = "Delete",
                            icon = painterResource(Res.drawable.delete_red),
                            onClick = { viewModel.sent(WordDetailsAction.Delete) }
                        )
                    )
                )
            }
        }

        // Content
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
            }
            return
        }

        if (state.userWord != null) {
            WordDetailsContent(
                userWord = state.userWord ?: throw IllegalStateException(),
                isPlayingSound = state.isPlayingSound,
                image = image,
                onPlaySound = { viewModel.sent(WordDetailsAction.PlaySound) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        state.errorMessage?.let {
            ErrorMessageBox(it)
        }
    }
}

@Composable
private fun WordDetailsContent(
    userWord: UserWord,
    image: ByteContent?,
    isPlayingSound: Boolean,
    onPlaySound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val word = userWord.word

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ImageFromBytes(
            imageBytes = image?.bytes,
            defaultPaint = painterResource(Res.drawable.image_icon),
            width = 200.dp,
            height = 200.dp,
            contentDescription = "Word Image"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category
        if (word.category != null) {
            Text(
                text = "Category: ${word.category}",
                color = AppTheme.PrimaryColor,
                fontSize = 16.sp
            )
        }

        // CEFR Level
        Text(
            text = "CEFR: ${word.cefr}",
            color = AppTheme.PrimaryColor,
            fontSize = 16.sp
        )

        // Original word
        Text(
            text = "${word.lang.upperShortName}: ${word.original}",
            color = AppTheme.PrimaryColor,
            fontSize = 24.sp
        )

        // Translation
        Text(
            text = "${word.translateLang.upperShortName}: ${word.translate}",
            color = AppTheme.PrimaryColor,
            fontSize = 24.sp
        )

        // Description
        if (word.description != null) {
            Text(
                text = "Definition: ${word.description}",
                color = AppTheme.PrimaryColor,
                fontSize = 14.sp
            )
        }

        // Dates
        Text(
            text = "Created: ${userWord.createdAt}",
            color = AppTheme.SecondaryText,
            fontSize = 12.sp
        )

        Text(
            text = "Last read: ${userWord.lastReadDate}",
            color = AppTheme.SecondaryText,
            fontSize = 12.sp
        )

        // Sound button
        if (word.soundLink != null) {
            Button(
                onClick = onPlaySound,
                enabled = !isPlayingSound,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryColor
                ),
                modifier = Modifier
                    .size(80.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.sound),
                    contentDescription = "Play sound",
                    tint = AppTheme.PrimaryBack,
                    modifier = Modifier.size(40.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.sound),
                    contentDescription = "No sound",
                    tint = AppTheme.PrimaryGray,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

