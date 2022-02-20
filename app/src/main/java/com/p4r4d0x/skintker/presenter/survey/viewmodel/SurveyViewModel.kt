package com.p4r4d0x.skintker.presenter.survey.viewmodel

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.data.repository.LogsRepository
import com.p4r4d0x.skintker.domain.log.LogState
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.domain.parsers.DataParser
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SurveyViewModel(
    private val addLogUseCase: AddLogUseCase,
    private val getLogUseCase: GetLogUseCase
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
            surveyInitialState = SurveyState.LogQuestions(questions)
            _uiState.value = surveyInitialState
        }
    }

    fun checkIfLogIsAlreadyInserted() {
        getLogUseCase.invoke(params = GetLogUseCase.Params(date = DataParser.getCurrentFormattedDate())) { log ->
            _logReported.value = log != null
        }
    }

    fun computeResult(
        userId: String,
        surveyQuestions: SurveyState.LogQuestions,
        resources: Resources
    ) {
        val answers = surveyQuestions.state.mapNotNull { it.answer }
        addLogUseCase.invoke(
            viewModelScope,
            params = AddLogUseCase.Params(
                userId,
                DataParser.createLogFromSurvey(answers, resources)
            )
        )
        _uiState.value = SurveyState.Result
    }

    fun updateCityValue(city: String) {
        _city.value = city
    }

    fun shouldAskForPermissions() {
        askForPermissions = false
    }
}