package com.p4r4d0x.skintker.presenter.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.data.repository.LogsRepository
import com.p4r4d0x.skintker.domain.DataParser.createLogFromSurvey
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.log.LogState
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getLogsUseCase: GetLogsUseCase,
    private val addLogUseCase: AddLogUseCase
) : ViewModel() {

    private val logsRepository = LogsRepository()

    private val _uiState = MutableLiveData<SurveyState>()
    val uiState: LiveData<SurveyState>
        get() = _uiState

    private val _logList = MutableLiveData<List<DailyLogBO>>()
    val logList: LiveData<List<DailyLogBO>>
        get() = _logList

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

    fun getLogs() {
        getLogsUseCase.invoke(viewModelScope) {
            _logList.value = it
        }
    }

    private fun addLog(log: DailyLogBO) {
        addLogUseCase.invoke(viewModelScope, params = AddLogUseCase.Params(log)) { added ->
            if (added) {
                getLogs()
            }
        }
    }

    fun computeResult(surveyQuestions: SurveyState.LogQuestions, resources: Resources) {
        val answers = surveyQuestions.state.mapNotNull { it.answer }
        addLog(createLogFromSurvey(answers, resources))
        val result = logsRepository.getSurveyResult(answers)
        _uiState.value = SurveyState.Result(surveyQuestions.surveyTitle, result)
    }
}