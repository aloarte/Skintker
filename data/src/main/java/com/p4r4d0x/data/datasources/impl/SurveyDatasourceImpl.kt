package com.p4r4d0x.data.datasources.impl

import com.example.data.R
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.domain.bo.PossibleAnswer
import com.p4r4d0x.domain.bo.Question
import com.p4r4d0x.domain.bo.Survey
import com.p4r4d0x.domain.utils.Constants.EIGHTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.FIFTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.FIRST_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.FOURTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.NINTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.SECOND_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.SEVENTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.SIXTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.THIRD_QUESTION_NUMBER

class SurveyDataSourceImpl : SurveyDataSource {
    override fun getSurvey(): Survey = jetpackSurvey
}

// Static data of questions
private val jetpackQuestions = mutableListOf(

    Question(
        id = FIRST_QUESTION_NUMBER,
        questionText = R.string.question_1_title,
        description = R.string.question_1_description,
        answer = PossibleAnswer.Slider(
            range = 1f..10f,
            steps = 5,
            defaultValue = 0.0f,
            startText = R.string.question_1_slide_start,
            neutralText = R.string.question_1_slide_mid,
            endText = R.string.question_1_slide_end
        )
    ),
    Question(
        id = SECOND_QUESTION_NUMBER,
        questionText = R.string.question_2_title,
        answer = PossibleAnswer.MultipleChoice(
            optionsStringRes = listOf(
                R.string.question_2_answer_1,
                R.string.question_2_answer_2,
                R.string.question_2_answer_3,
                R.string.question_2_answer_4,
                R.string.question_2_answer_5,
                R.string.question_2_answer_6,
                R.string.question_2_answer_7,
                R.string.question_2_answer_8,
                R.string.question_2_answer_9,
                R.string.question_2_answer_10,
                R.string.question_2_answer_11,
                R.string.question_2_answer_12
            )
        ),
    ),
    Question(
        id = THIRD_QUESTION_NUMBER,
        questionText = R.string.question_3_title,
        description = R.string.question_3_description,
        answer = PossibleAnswer.Slider(
            range = 1f..10f,
            steps = 5,
            defaultValue = 0.0f,
            startText = R.string.question_3_slide_start,
            neutralText = R.string.question_3_slide_mid,
            endText = R.string.question_3_slide_end
        )
    ),
    Question(
        id = FOURTH_QUESTION_NUMBER,
        questionText = R.string.question_4_title,
        answer = PossibleAnswer.SingleChoice(
            listOf(
                R.string.question_4_answer_1,
                R.string.question_4_answer_2,
                R.string.question_4_answer_3,
                R.string.question_4_answer_4
            )
        )
    ),
    Question(
        id = FIFTH_QUESTION_NUMBER,
        questionText = R.string.question_5_title,
        answer = PossibleAnswer.MultipleChoice(
            listOf(
                R.string.question_5_answer_1,
                R.string.question_5_answer_2,
                R.string.question_5_answer_3,
                R.string.question_5_answer_4,
                R.string.question_5_answer_5,
                R.string.question_5_answer_6,
                R.string.question_5_answer_7,
                R.string.question_5_answer_8,
                R.string.question_5_answer_9
            )
        )
    ),
    Question(
        id = SIXTH_QUESTION_NUMBER,
        questionText = R.string.question_6_title,
        description = R.string.question_6_description,
        answer = PossibleAnswer.DoubleSlider(
            range = 1f..5f,
            steps = 3,
            defaultValueFirstSlider = 3f,
            startTextFirstSlider = R.string.question_6_slide_1_start,
            endTextFirstSlider = R.string.question_6_slide_1_end,
            defaultValueSecondSlider = 3f,
            startTextSecondSlider = R.string.question_6_slide_2_start,
            endTextSecondSlider = R.string.question_6_slide_2_end
        )
    ),
    Question(
        id = SEVENTH_QUESTION_NUMBER,
        questionText = R.string.question_7_title,
        description = R.string.question_7_description,
        answer = PossibleAnswer.SingleTextInputSingleChoice(
            hint = "",
            maxCharacters = 40,
            optionsStringRes = listOf(
                R.string.question_7_answer_1,
                R.string.question_7_answer_2
            )
        ), permissionsRequired = listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    ),
    Question(
        id = EIGHTH_QUESTION_NUMBER,
        questionText = R.string.question_8_title,
        description = R.string.question_8_description,
        answer = PossibleAnswer.MultipleChoice(
            optionsStringRes = listOf(
                R.string.question_8_answer_1,
                R.string.question_8_answer_2,
                R.string.question_8_answer_3,
                R.string.question_8_answer_4,
                R.string.question_8_answer_5,
                R.string.question_8_answer_6,
                R.string.question_8_answer_7,
                R.string.question_8_answer_8,
                R.string.question_8_answer_9,
                R.string.question_8_answer_10,
                R.string.question_8_answer_11,
                R.string.question_8_answer_12,
                R.string.question_8_answer_13,
                R.string.question_8_answer_14,
                R.string.question_8_answer_15,
                R.string.question_8_answer_16,
                R.string.question_8_answer_17,
                R.string.question_8_answer_18
            )
        )
    ),
    Question(
        id = NINTH_QUESTION_NUMBER,
        questionText = R.string.question_9_title,
        description = R.string.question_9_description,
        answer = PossibleAnswer.MultipleChoice(
            optionsStringRes = listOf(
                R.string.question_9_answer_1,
                R.string.question_9_answer_2,
                R.string.question_9_answer_3,
                R.string.question_9_answer_4,
                R.string.question_9_answer_5,
                R.string.question_9_answer_6,
                R.string.question_9_answer_7,
                R.string.question_9_answer_8,
                R.string.question_9_answer_9,
                R.string.question_9_answer_10,
                R.string.question_9_answer_11,
                R.string.question_9_answer_12,
                R.string.question_9_answer_13
            )
        )
    )


).toList()

private val jetpackSurvey = Survey(
    questions = jetpackQuestions
)