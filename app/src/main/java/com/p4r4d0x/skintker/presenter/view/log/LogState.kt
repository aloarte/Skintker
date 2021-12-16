package com.p4r4d0x.skintker.presenter.view.log

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class LogState(
    val question: Question,
    val index: Int,
    val totalCount: Int,
    val showPrevious: Boolean,
    val showDone: Boolean
) {
    var enableNext by mutableStateOf(false)
    var answer by mutableStateOf<Answer<*>?>(null)
}

sealed class SurveyState {
    data class LogQuestions(
        @StringRes val surveyTitle: Int,
        val state: List<LogState>
    ) : SurveyState() {
        var currentIndex by mutableStateOf(0)
    }

    data class Result(
        @StringRes val surveyTitle: Int,
        val surveyResult: SurveyResult
    ) : SurveyState()
}
