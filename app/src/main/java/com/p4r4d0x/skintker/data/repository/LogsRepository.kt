/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.p4r4d0x.skintker.data.repository

import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants.FIFTH_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.FIRST_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.FOURTH_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.SECOND_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.SEVENTH_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.SIXTH_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.THIRD_QUESTION_NUMBER
import com.p4r4d0x.skintker.domain.log.PossibleAnswer
import com.p4r4d0x.skintker.domain.log.Question
import com.p4r4d0x.skintker.domain.log.Survey

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
                R.string.question_2_answer_8
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
                R.string.question_4_answer_3
            )
        )
    ),
    Question(
        id = FIFTH_QUESTION_NUMBER,
        questionText = R.string.question_5_title,
        description = R.string.question_5_description,
        answer = PossibleAnswer.DoubleSlider(
            range = 1f..10f,
            steps = 3,
            defaultValueFirstSlider = 5.5f,
            startTextFirstSlider = R.string.question_5_slide_1_start,
            endTextFirstSlider = R.string.question_5_slide_1_end,
            defaultValueSecondSlider = 5.5f,
            startTextSecondSlider = R.string.question_5_slide_2_start,
            endTextSecondSlider = R.string.question_5_slide_2_end
        )
    ),
    Question(
        id = SIXTH_QUESTION_NUMBER,
        questionText = R.string.question_6_title,
        description = R.string.question_6_description,
        answer = PossibleAnswer.SingleTextInputSingleChoice(
            hint = "R.string.question_6_hint",
            maxCharacters = 40,
            optionsStringRes = listOf(
                R.string.question_6_answer_1,
                R.string.question_6_answer_2
            )
        ), permissionsRequired = listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    ),
    Question(
        id = SEVENTH_QUESTION_NUMBER,
        questionText = R.string.question_7_title,
        answer = PossibleAnswer.MultipleChoice(
            optionsStringRes = listOf(
                R.string.question_7_answer_1,
                R.string.question_7_answer_2,
                R.string.question_7_answer_3,
                R.string.question_7_answer_4,
                R.string.question_7_answer_5,
                R.string.question_7_answer_6
            )
        ),
    ),


    ).toList()

private val jetpackSurvey = Survey(
    questions = jetpackQuestions
)

class LogsRepository {
    fun getSurvey() = jetpackSurvey
}