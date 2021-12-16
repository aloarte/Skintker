package com.p4r4d0x.skintker.presenter.view.log

import androidx.annotation.StringRes

data class SurveyResult(
    val library: String,
    @StringRes val result: Int,
    @StringRes val description: Int
)

data class Survey(
    @StringRes val title: Int,
    val questions: List<Question>
)

data class Question(
    val id: Int,
    @StringRes val questionText: Int,
    val answer: PossibleAnswer,
    @StringRes val description: Int? = null
)

sealed class PossibleAnswer {
    data class SingleChoice(val optionsStringRes: List<Int>) : PossibleAnswer()
    data class MultipleChoice(val optionsStringRes: List<Int>) : PossibleAnswer()
    data class SingleTextInput(val hint: Int, val maxCharacters: Int) : PossibleAnswer()
    data class Slider(
        val range: ClosedFloatingPointRange<Float>,
        val steps: Int,
        @StringRes val startText: Int,
        @StringRes val endText: Int,
        @StringRes val neutralText: Int,
        val defaultValue: Float = 5.5f
    ) : PossibleAnswer()

    data class DoubleSlider(
        val range: ClosedFloatingPointRange<Float>,
        val steps: Int,
        val defaultValueFirstSlider: Float = 5.5f,
        @StringRes val startTextFirstSlider: Int,
        @StringRes val endTextFirstSlider: Int,
        val defaultValueSecondSlider: Float = 5.5f,
        @StringRes val startTextSecondSlider: Int,
        @StringRes val endTextSecondSlider: Int
    ) : PossibleAnswer()
}

/**
 * Sealed class with the possible answers in the survey
 */
sealed class Answer<T : PossibleAnswer> {
    data class SingleChoice(@StringRes val answer: Int) : Answer<PossibleAnswer.SingleChoice>()
    data class MultipleChoice(val answersStringRes: Set<Int>) :
        Answer<PossibleAnswer.MultipleChoice>()

    data class SingleTextInput(val answers: String) : Answer<PossibleAnswer.SingleTextInput>()
    data class Slider(val answerValue: Float) : Answer<PossibleAnswer.Slider>()
    data class DoubleSlider(val answerValueFirstSlider: Float, val answerValueSecondSlider: Float) :
        Answer<PossibleAnswer.Slider>()

}

/**
 * Add or remove an answer from the list of selected answers depending on whether the answer was
 * selected or deselected.
 */
fun Answer.MultipleChoice.withAnswerSelected(
    @StringRes answer: Int,
    selected: Boolean
): Answer.MultipleChoice {
    val newStringRes = answersStringRes.toMutableSet()
    if (!selected) {
        newStringRes.remove(answer)
    } else {
        newStringRes.add(answer)
    }
    return Answer.MultipleChoice(newStringRes)
}
