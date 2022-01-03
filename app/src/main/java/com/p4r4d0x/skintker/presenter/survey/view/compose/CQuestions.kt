package com.p4r4d0x.skintker.presenter.home.view.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.p4r4d0x.skintker.domain.log.Answer
import com.p4r4d0x.skintker.domain.log.PossibleAnswer
import com.p4r4d0x.skintker.domain.log.Question
import com.p4r4d0x.skintker.domain.log.withAnswerSelected

@Composable
fun QuestionContent(
    question: Question,
    answer: Answer<*>?,
    onAnswer: (Answer<*>) -> Unit,
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
            QuestionBody(question, answer, onAnswer, modifier = Modifier.fillParentMaxWidth())
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

@Composable
private fun QuestionBody(
    question: Question,
    answer: Answer<*>?,
    onAnswer: (Answer<*>) -> Unit,
    modifier: Modifier
) {
    when (question.answer) {
        is PossibleAnswer.SingleChoice -> SingleChoiceQuestion(
            possibleAnswer = question.answer,
            answer = answer as Answer.SingleChoice?,
            onAnswerSelected = { answerValue -> onAnswer(Answer.SingleChoice(answerValue)) },
            modifier = modifier
        )
        is PossibleAnswer.MultipleChoice -> MultipleChoiceQuestion(
            possibleAnswer = question.answer,
            answer = answer as Answer.MultipleChoice?,
            onAnswerSelected = { newAnswer, selected ->
                // create the answer if it doesn't exist or
                // update it based on the user's selection
                if (answer == null) {
                    onAnswer(Answer.MultipleChoice(setOf(newAnswer)))
                } else {
                    onAnswer(answer.withAnswerSelected(newAnswer, selected))
                }
            },
            modifier = modifier
        )
        is PossibleAnswer.Slider -> SliderQuestion(
            possibleAnswer = question.answer,
            answer = answer as Answer.Slider?,
            onAnswerSelected = { onAnswer(Answer.Slider(it)) },
            modifier = modifier
        )
        is PossibleAnswer.DoubleSlider -> DoubleSliderQuestion(
            possibleAnswer = question.answer,
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
            possibleAnswer = question.answer,
            answer = answer as Answer.SingleTextInput?,
            onAnswerSelected = {
                onAnswer(
                    Answer.SingleTextInput(it)
                )
            },
            modifier = modifier.padding(10.dp)
        )
        is PossibleAnswer.SingleTextInputSingleChoice -> SingleTextInputSingleChoice(
            possibleAnswer = question.answer,
            answer = answer as Answer.SingleTextInputSingleChoice?,
            onAnswerSelected = { values, hint ->
                onAnswer(
                    Answer.SingleTextInputSingleChoice(values, hint)
                )
            },
            modifier = modifier.padding(10.dp)
        )
    }
}
