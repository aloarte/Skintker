package com.p4r4d0x.skintker.presenter.survey.view.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel

@ExperimentalPermissionsApi
@Composable
fun QuestionContent(
    viewModel: SurveyViewModel?,
    question: Question,
    answer: Answer<*>?,
    shouldAskPermissions: Boolean,
    onDoNotAskForPermissions: () -> Unit,
    onAnswer: (Answer<*>) -> Unit,
    onAction: (SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            QuestionTitle(question.questionText)
            Spacer(modifier = Modifier.height(10.dp))
            QuestionDescription(question.description)
            Spacer(modifier = Modifier.height(10.dp))
            QuestionBodyPermissions(
                viewModel = viewModel,
                question = question,
                answer = answer,
                shouldAskPermissions = shouldAskPermissions,
                onDoNotAskForPermissions = onDoNotAskForPermissions,
                onAnswer = onAnswer,
                onAction = onAction,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
private fun QuestionTitle(@StringRes title: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp)
        )
    }
}

@Composable
private fun QuestionDescription(description: Int?) {
    if (description != null) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(id = description),
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(bottom = 18.dp, start = 10.dp, end = 10.dp)
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
private fun QuestionBodyPermissions(
    viewModel: SurveyViewModel?,
    question: Question,
    answer: Answer<*>?,
    shouldAskPermissions: Boolean,
    onDoNotAskForPermissions: () -> Unit,
    onAnswer: (Answer<*>) -> Unit,
    onAction: (SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    if (question.permissionsRequired.isEmpty()) {
        QuestionBody(viewModel, question, answer, onAnswer, onAction, modifier)
    } else {
        val permissionsContentModifier = modifier.padding(horizontal = 20.dp)
        val multiplePermissionsState =
            rememberMultiplePermissionsState(question.permissionsRequired)

        if (!shouldAskPermissions) {
            QuestionBody(viewModel, question, answer, onAnswer, onAction, modifier)
        } else {
            when {
                // If all permissions are granted, then show the question
                multiplePermissionsState.allPermissionsGranted -> {
                    QuestionBody(viewModel, question, answer, onAnswer, onAction, modifier)
                }
                multiplePermissionsState.shouldShowRationale -> {
                    PermissionsRationale(
                        question = question,
                        multiplePermissionsState = multiplePermissionsState,
                        modifier = permissionsContentModifier,
                        onDoNotAskForPermissions = onDoNotAskForPermissions
                    )
                }
                // If the criteria above hasn't been met, the user denied some permission, but show the question
                else -> {
                    QuestionBody(viewModel, question, answer, onAnswer, onAction, modifier)
                }
            }
        }
    }
}

@Composable
private fun QuestionBody(
    viewModel: SurveyViewModel?,
    question: Question,
    answer: Answer<*>?,
    onAnswer: (Answer<*>) -> Unit,
    onAction: (SurveyActionType) -> Unit,
    modifier: Modifier
) {
    when (question.answer) {
        is PossibleAnswer.SingleChoice -> SingleChoiceQuestion(
            possibleAnswer = question.answer as PossibleAnswer.SingleChoice,
            answer = answer as Answer.SingleChoice?,
            onAnswerSelected = { answerValue -> onAnswer(Answer.SingleChoice(answerValue)) },
            modifier = modifier
        )
        is PossibleAnswer.MultipleChoice -> MultipleChoiceQuestion(
            possibleAnswer = question.answer as PossibleAnswer.MultipleChoice,
            answer = answer as Answer.MultipleChoice?,
            onAnswerSelected = { newAnswer, selected ->
                if (answer == null) {
                    onAnswer(Answer.MultipleChoice(setOf(newAnswer)))
                } else {
                    onAnswer(answer.withAnswerSelected(newAnswer, selected))
                }
            },
            modifier = modifier
        )
        is PossibleAnswer.Slider -> SliderQuestion(
            possibleAnswer = question.answer as PossibleAnswer.Slider,
            answer = answer as Answer.Slider?,
            onAnswerSelected = { onAnswer(Answer.Slider(it)) },
            modifier = modifier
        )
        is PossibleAnswer.DoubleSlider -> DoubleSliderQuestion(
            possibleAnswer = question.answer as PossibleAnswer.DoubleSlider,
            answer = answer as Answer.DoubleSlider?,
            onAnswerSelected = { firstSlideValue, secondSlideValue ->
                onAnswer(
                    Answer.DoubleSlider(
                        firstSlideValue,
                        secondSlideValue
                    )
                )
            },
            modifier = modifier
        )
        is PossibleAnswer.SingleTextInput -> SingleTextInput(
            possibleAnswer = question.answer as PossibleAnswer.SingleTextInput,
            answer = answer as Answer.SingleTextInput?,
            onAnswerSelected = {
                onAnswer(
                    Answer.SingleTextInput(it)
                )
            },
            modifier = modifier.padding(10.dp)
        )
        is PossibleAnswer.SingleTextInputSingleChoice -> SingleTextInputSingleChoice(
            viewModel = viewModel,
            possibleAnswer = question.answer as PossibleAnswer.SingleTextInputSingleChoice,
            answer = answer as Answer.SingleTextInputSingleChoice?,
            onAnswerSelected = { values, hint ->
                onAnswer(
                    Answer.SingleTextInputSingleChoice(values, hint)
                )
            },
            onAction = onAction,
            modifier = modifier.padding(10.dp)
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionsRationale(
    question: Question,
    multiplePermissionsState: MultiplePermissionsState,
    modifier: Modifier = Modifier,
    onDoNotAskForPermissions: () -> Unit
) {
    Column(modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        QuestionTitle(question.questionText)
        Spacer(modifier = Modifier.height(32.dp))
        val rationaleId =
            question.permissionsRationaleText ?: R.string.permissions_rationale
        Text(stringResource(id = rationaleId))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        ) {
            Text(stringResource(R.string.request_permissions))
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onDoNotAskForPermissions) {
            Text(stringResource(R.string.do_not_ask_permissions))
        }
    }
}