package vm.words.ua.learning.ui.screans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.SingleSelectInput
import vm.words.ua.core.utils.*
import vm.words.ua.di.rememberInstance
import vm.words.ua.learning.domain.models.enums.PlanStatus
import vm.words.ua.learning.ui.actions.LearningPlanAction
import vm.words.ua.learning.ui.components.CounterRow
import vm.words.ua.learning.ui.components.ProgressPieChart
import vm.words.ua.learning.ui.states.LearningPlanState
import vm.words.ua.learning.ui.vms.LearningPlanVm
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LearningPlanScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier,

    ) {
    val viewModel: LearningPlanVm = rememberInstance<LearningPlanVm>()
    val state by viewModel.state.collectAsState()


    val handleBackPress: () -> Unit = {
        if (viewModel.state.value.detailsState == null) {
            navController.popBackStack()
        } else {
            viewModel.sent(LearningPlanAction.ClearDetailsState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppToolBar(
            title = "Learning Plan",
            onBackClick = handleBackPress,
            showAdditionalButton = true,
            additionalButtonVector = Icons.Default.Edit,
            onAdditionalClick = { viewModel.sent(LearningPlanAction.ModifyPlan) }

        )
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
            }
            return@Column
        }

        Column(modifier = Modifier.widthIn(max = rememberInterfaceMaxWidth()).fillMaxWidth()) {
            if (state.learningPlan == null && state.detailsState == null) {
                CreateBtn(
                    onClick = {
                        viewModel.sent(LearningPlanAction.ModifyPlan)
                    }
                )
                return@Column
            }

            if (state.detailsState != null) {
                ModifyPlan(
                    viewModel = viewModel,
                    state = state
                )
                return@Column
            }

            if (state.learningPlan == null) {
                return@Column
            }

            PlanViewer(state)
        }
    }

    state.errorMessage?.let {
        ErrorMessageBox(it)
    }
}

@Composable
private fun CreateBtn(onClick: () -> Unit) {
    val fontSize = rememberFontSize() * 1.6f

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            TextButton(
                onClick = onClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.PrimaryColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(rememberIconSize())
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Plan",
                    fontSize = fontSize
                )
            }
        }
    }
}

@Composable
private fun ModifyPlan(
    viewModel: LearningPlanVm,
    state: LearningPlanState
) {

    val plan = state.detailsState
    val columns = if (isNotPhoneFormat()) 2 else 1
    val btnTitle = remember(state.type) {
        if (state.type == PlanStatus.EDIT) "Save Changes" else "Create Plan"
    }


    if (plan == null) {
        return
    }


    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            item(span = { GridItemSpan(columns) }) {
                Column {
                    Text(
                        "Words per day",
                        color = AppTheme.PrimaryColor,
                        fontSize = rememberFontSize(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CounterRow(
                        count = plan.wordsPerDay,
                        onMinusClick = {
                            viewModel.sent(LearningPlanAction.DecreaseWordsPerDay)
                        },
                        onPlusClick = {
                            viewModel.sent(LearningPlanAction.IncreaseWordsPerDay)
                        },
                        minValue = 1,
                        maxValue = 100
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                SingleSelectInput(
                    value = plan.nativeLang,
                    items = Language.entries.filter { it != Language.UNDEFINED },
                    label = "Native language",
                    toLabel = { it.titleCase },
                    showNone = false,
                    noneLabel = "",
                    onSelect = {
                        viewModel.sent(LearningPlanAction.UpdateNativeLang(it ?: return@SingleSelectInput))
                    }
                )
            }


            item {
                SingleSelectInput(
                    value = plan.learningLang,
                    items = Language.entries.filter { it != Language.UNDEFINED },
                    label = "Learning language",
                    toLabel = { it.titleCase },
                    showNone = false,
                    noneLabel = "",
                    onSelect = {
                        viewModel.sent(LearningPlanAction.UpdateLearningLang(it ?: return@SingleSelectInput))
                    }
                )
            }

            // 5. Multi select CEFR using generic MultiSelect component
            item(span = { GridItemSpan(columns) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SingleSelectInput(
                        value = plan.cefr,
                        items = CEFR.entries,
                        label = "Language level",
                        toLabel = { it.name },
                        showNone = false,
                        noneLabel = "",
                        modifier = Modifier.widthIn(max = 500.dp),
                        onSelect = {
                            viewModel.sent(LearningPlanAction.UpdateCefr(it ?: return@SingleSelectInput))
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.sent(LearningPlanAction.SubmitPlan)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.PrimaryColor,
                contentColor = AppTheme.PrimaryBack
            )
        ) {
            Text(btnTitle, fontSize = rememberFontSize())
        }
    }
}

@Composable
private fun PlanViewer(state: LearningPlanState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Words learned today
        Text(
            text = "Words learned today",
            color = AppTheme.PrimaryColor,
            fontSize = rememberFontSize()
        )

        Text(
            text = state.let {
                "${it.learnedWordsToDay} / ${it.learningPlan?.wordsPerDay ?: 0}"
            },
            color = AppTheme.PrimaryColor,
            fontSize = rememberFontSize() * 1.3f
        )

        Spacer(modifier = Modifier.height(10.dp))

        ProgressPieChart(
            learned = state.learnedWordsToDay,
            need = state.learningPlan?.wordsPerDay ?: 0
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Words count labels
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Added words",
                color = AppTheme.PrimaryColor,
                fontSize = rememberLabelFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Learned words",
                color = AppTheme.PrimaryColor,
                fontSize = rememberLabelFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        // Words count values
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = state.addedWords.toString(),
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = state.learnedWords.toString(),
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // CEFR level
        Text(
            text = "CEFR level",
            color = AppTheme.PrimaryColor,
            fontSize = rememberLabelFontSize()
        )


        Text(
            text = state.learningPlan?.cefr?.name.orEmpty(),
            color = AppTheme.PrimaryColor,
            fontSize = rememberFontSize()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Language labels
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Native language",
                color = AppTheme.PrimaryColor,
                fontSize = rememberLabelFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Learning language",
                color = AppTheme.PrimaryColor,
                fontSize = rememberLabelFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }


        // Language values
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = state.learningPlan?.nativeLang?.titleCase.orEmpty(),
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = state.learningPlan?.learningLang?.titleCase.orEmpty(),
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

