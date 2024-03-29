package com.p4r4d0x.skintker.presenter.survey

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.p4r4d0x.domain.bo.Answer
import com.p4r4d0x.domain.bo.Question
import java.util.*

@Stable
data class LogState(
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
    //Initial state. Prompt the user a date selector or use the current date.
    object PickDate : SurveyState()

    //State of the survey. Display the complete survey.
    data class Questions(
        val state: List<LogState>,
        val date: Date
    ) : SurveyState() {
        var currentIndex by mutableStateOf(0)
    }

    //Final state. Process the survey.
    object Result : SurveyState()
    object ResultError : SurveyState()
}
