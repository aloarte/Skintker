package com.p4r4d0x.skintker.presenter.survey.viewmodel

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.data.parsers.DataParser
import com.p4r4d0x.domain.usecases.AddLogUseCase
import com.p4r4d0x.domain.usecases.GetLogUseCase
import com.p4r4d0x.domain.usecases.GetSurveyUseCase
import com.p4r4d0x.skintker.presenter.survey.LogState
import com.p4r4d0x.skintker.presenter.survey.SurveyState
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class SurveyViewModel(
    private val addLogUseCase: AddLogUseCase,
    private val getLogUseCase: GetLogUseCase,
    private val getSurveyUseCase: GetSurveyUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<SurveyState>()
    val uiState: MutableLiveData<SurveyState>
        get() = _uiState

    private val _city = MutableStateFlow("")
    val city: MutableStateFlow<String>
        get() = _city

    private val _logReported = MutableLiveData<Boolean>()
    val logReported: LiveData<Boolean>
        get() = _logReported

    var askForPermissions by mutableStateOf(true)
        private set

    fun checkIfLogIsAlreadyInserted() {
        getLogUseCase.invoke(
            scope = viewModelScope,
            params = GetLogUseCase.Params(date = DataParser.getCurrentFormattedDate())
        ) { log ->
            _logReported.value = log != null
        }
    }

    /**
     * Process the result of the questions parsing the data.
     * Change the UI state to SurveyState.Result
     */
    fun computeResult(
        userId: String,
        surveyQuestions: SurveyState.Questions,
        resources: Resources
    ) {
        val answers = surveyQuestions.state.mapNotNull { it.answer }
        addLogUseCase.invoke(
            scope = viewModelScope,
            params = AddLogUseCase.Params(
                userId,
                DataParser.createLogFromSurvey(surveyQuestions.date, answers, resources)
            )
        ) {
            _uiState.value = if (it) SurveyState.Result else SurveyState.ResultError
        }
    }

    /**
     * Pick the date.
     * Change the UI state to SurveyState.PickDate
     */
    fun loadDate(pickDate: Boolean) {
        if (pickDate) {
            _uiState.value = SurveyState.PickDate
        } else {
            loadQuestions()
        }
    }

    /**
     * Load the questions.
     *  Change the UI state to SurveyState.Questions
     */
    fun loadQuestions(date: Date = DataParser.getCurrentFormattedDate()) {
        getSurveyUseCase.invoke(scope = viewModelScope) { survey ->
            val questions: List<LogState> = survey.questions.mapIndexed { index, question ->
                val showPrevious = index > 0
                val showDone = index == survey.questions.size - 1
                LogState(
                    question = question,
                    index = index,
                    totalCount = survey.questions.size,
                    showPrevious = showPrevious,
                    showDone = showDone
                )
            }
            _uiState.value = SurveyState.Questions(questions, date)
        }
    }

    fun updateCityValue(city: String) {
        _city.value = city
    }

    fun shouldAskForPermissions() {
        askForPermissions = false
    }
}