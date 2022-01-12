package com.p4r4d0x.skintker.presenter.survey.viewmodel

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.data.repository.LogsRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.log.LogState
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.domain.parsers.DataParser
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import kotlinx.coroutines.launch

class SurveyViewModel(
    private val addLogUseCase: AddLogUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<SurveyState>()
    val uiState: MutableLiveData<SurveyState>
        get() = _uiState

    private val _city = MutableLiveData<String>()
    val city: MutableLiveData<String>
        get() = _city

    var askForPermissions by mutableStateOf(true)
        private set

    private val logsRepository = LogsRepository()
    private lateinit var surveyInitialState: SurveyState

    init {
        viewModelScope.launch {
            val survey = logsRepository.getSurvey()

            // Create the default questions state based on the survey questions
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
            surveyInitialState = SurveyState.LogQuestions(survey.title, questions)
            _uiState.value = surveyInitialState
        }
    }

    fun computeResult(surveyQuestions: SurveyState.LogQuestions, resources: Resources) {
        val answers = surveyQuestions.state.mapNotNull { it.answer }
        addLog(DataParser.createLogFromSurvey(answers, resources))
        val result = logsRepository.getSurveyResult(answers)
        _uiState.value = SurveyState.Result(surveyQuestions.surveyTitle, result)
    }


    private fun addLog(log: DailyLogBO) {
        addLogUseCase.invoke(viewModelScope, params = AddLogUseCase.Params(log))
    }

    fun updateCityValue(city: String) {
        _city.value = city
    }

    fun shouldAskForPermissions() {
        askForPermissions = false
    }
}