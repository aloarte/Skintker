package com.p4r4d0x.skintker.presenter.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.p4r4d0x.domain.bo.PossibleAnswer
import com.p4r4d0x.domain.bo.Question
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.survey.view.compose.QuestionContent
import com.p4r4d0x.skintker.theme.SkintkerTheme


@ExperimentalPermissionsApi
@Preview
@Composable
fun MultipleAnswerQuestionPreview() {
    val question = Question(
        id = 1,
        questionText = R.string.multiple_answer_title,
        answer = PossibleAnswer.MultipleChoice(
            optionsStringRes = listOf(
                R.string.multiple_answer_1,
                R.string.multiple_answer_2,
                R.string.multiple_answer_3
            )
        ),
        description = R.string.multiple_description
    )
    SkintkerTheme {
        QuestionContent(
            viewModel = null,
            question = question,
            answer = null,
            shouldAskPermissions = false,
            onDoNotAskForPermissions = {},
            onAnswer = {},
            onAction = {}
        )
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
fun SingleAnswerQuestionPreview() {
    val question = Question(
        id = 2,
        questionText = R.string.single_answer_title,
        answer = PossibleAnswer.SingleChoice(
            optionsStringRes = listOf(
                R.string.single_answer_1,
                R.string.single_answer_2,
                R.string.single_answer_3
            )
        ),
        description = R.string.single_description
    )
    SkintkerTheme {
        QuestionContent(
            viewModel = null,
            question = question,
            answer = null,
            shouldAskPermissions = false,
            onDoNotAskForPermissions = {},
            onAnswer = {},
            onAction = {}
        )
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
fun SlideAnswerQuestionPreview() {
    val question = Question(
        id = 3,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.Slider(
            range = 1f..10f,
            steps = 5,
            startText = R.string.slide_point_1,
            endText = R.string.slide_point_1,
            neutralText = R.string.slide_point_1
        ),
        description = R.string.slide_description
    )
    SkintkerTheme {
        QuestionContent(
            viewModel = null,
            question = question,
            answer = null,
            shouldAskPermissions = false,
            onDoNotAskForPermissions = {},
            onAnswer = {},
            onAction = {}
        )
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
fun DoubleSlideAnswerQuestionPreview() {
    val question = Question(
        id = 4,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.DoubleSlider(
            range = 1f..10f,
            steps = 5,
            defaultValueFirstSlider = 2.0f,
            startTextFirstSlider = R.string.slide_point_1,
            endTextFirstSlider = R.string.slide_point_2,
            defaultValueSecondSlider = 8.0f,
            startTextSecondSlider = R.string.slide_point_1,
            endTextSecondSlider = R.string.slide_point_2,
        ),
        description = R.string.slide_description
    )
    SkintkerTheme {
        QuestionContent(
            viewModel = null,
            question = question,
            answer = null,
            shouldAskPermissions = false,
            onDoNotAskForPermissions = {},
            onAnswer = {},
            onAction = {}
        )
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
fun SingleTextInputAnswerQuestionPreview() {
    val question = Question(
        id = 5,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.SingleTextInput(
            hint = R.string.slide_point_2,
            maxCharacters = 40
        ),
        description = R.string.slide_description
    )
    SkintkerTheme {
        QuestionContent(
            viewModel = null,
            question = question,
            answer = null,
            shouldAskPermissions = false,
            onDoNotAskForPermissions = {},
            onAnswer = {},
            onAction = {}
        )
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
fun SingleTextInputSingleChoiceAnswerQuestionPreview() {
    val question = Question(
        id = 5,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.SingleTextInputSingleChoice(
            hint = stringResource(id = R.string.slide_point_2),
            maxCharacters = 40,
            optionsStringRes = listOf(
                R.string.single_answer_1,
                R.string.single_answer_2,
                R.string.single_answer_3
            )
        ),
        description = R.string.single_description
    )
    SkintkerTheme {
        QuestionContent(
            viewModel = null,
            question = question,
            answer = null,
            shouldAskPermissions = false,
            onDoNotAskForPermissions = {},
            onAnswer = {},
            onAction = {}
        )
    }
}