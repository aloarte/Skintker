package com.p4r4d0x.skintker.presenter.view.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import com.p4r4d0x.skintker.domain.log.LogState
import com.p4r4d0x.skintker.domain.log.SurveyState

@Composable
fun LogQuestionScreen(
    logQuestions: SurveyState.LogQuestions,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit
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
                    question = questionState.question,
                    answer = questionState.answer,
                    onAnswer = {
                        questionState.answer = it
                        questionState.enableNext = true
                    },
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
fun SurveyResultScreen(
    result: SurveyState.Result,
    onDonePressed: () -> Unit
) {
    Surface {
        Scaffold(
            content = { innerPadding ->
                val modifier = Modifier.padding(innerPadding)
                SurveyResultContent(result = result, modifier = modifier)
            },
            bottomBar = {
                OutlinedButton(
                    onClick = { onDonePressed() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        )
    }
}

@Composable
private fun SurveyResultContent(result: SurveyState.Result, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(44.dp))
            Text(
                text = result.surveyResult.library,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text = stringResource(
                    result.surveyResult.result,
                    result.surveyResult.library
                ),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(20.dp)
            )
            Text(
                text = stringResource(result.surveyResult.description),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
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
                    Text(text = stringResource(id = R.string.previous))
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
                    Text(text = stringResource(id = R.string.done))
                }
            } else {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = onNextPressed,
                    enabled = state.enableNext
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
}