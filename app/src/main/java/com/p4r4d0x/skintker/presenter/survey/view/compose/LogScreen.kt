package com.p4r4d0x.skintker.presenter.survey.view.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.log.Answer
import com.p4r4d0x.skintker.domain.log.LogState
import com.p4r4d0x.skintker.domain.log.SurveyActionType
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.theme.progressIndicatorBackground

@ExperimentalPermissionsApi
@Composable
fun LogQuestionScreen(
    viewModel: SurveyViewModel,
    logQuestions: SurveyState.LogQuestions,
    shouldAskPermissions: Boolean,
    onDoNotAskForPermissions: () -> Unit,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit,
    onAction: (SurveyActionType) -> Unit,

    ) {

    val questionState = remember(logQuestions.currentIndex) {
        logQuestions.state[logQuestions.currentIndex]
    }

    Surface {
        Scaffold(
            topBar = {
                SurveyTopAppBar(
                    questionIndex = questionState.index,
                    totalQuestionsCount = questionState.totalCount,
                    onBackPressed = onBackPressed
                )
            },
            content = { innerPadding ->
                QuestionContent(
                    viewModel = viewModel,
                    question = questionState.question,
                    answer = questionState.answer,
                    shouldAskPermissions = shouldAskPermissions,
                    onDoNotAskForPermissions = onDoNotAskForPermissions,
                    onAnswer = {
                        questionState.answer = it
                        questionState.enableNext =
                            (questionState.answer as? Answer.SingleTextInputSingleChoice)?.let { answer ->
                                answer.input != "" && answer.answer != -1
                            } ?: run { true }
                    },
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            },
            bottomBar = {
                SurveyBottomBar(
                    state = questionState,
                    onPreviousPressed = { logQuestions.currentIndex-- },
                    onNextPressed = { logQuestions.currentIndex++ },
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}

@Composable
private fun SurveyTopAppBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onBackPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TopAppBarTitle(
                questionIndex = questionIndex,
                totalQuestionsCount = totalQuestionsCount,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .align(Alignment.Center)
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            backgroundColor = MaterialTheme.colors.progressIndicatorBackground
        )
    }
}

@Composable
private fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val totalStyle = MaterialTheme.typography.caption.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${questionIndex + 1}")
        }
        withStyle(style = totalStyle) {
            append(stringResource(R.string.question_count, totalQuestionsCount))
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}

@Composable
private fun SurveyBottomBar(
    state: LogState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit
) {
    Surface(
        elevation = 7.dp,
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (state.showPrevious) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = onPreviousPressed
                ) {
                    Text(text = stringResource(id = R.string.btn_previous))
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (state.showDone) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = onDonePressed,
                    enabled = state.enableNext
                ) {
                    Text(text = stringResource(id = R.string.btn_done))
                }
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = onNextPressed,
                    enabled = state.enableNext
                ) {
                    Text(text = stringResource(id = R.string.btn_next))
                }
            }
        }
    }
}