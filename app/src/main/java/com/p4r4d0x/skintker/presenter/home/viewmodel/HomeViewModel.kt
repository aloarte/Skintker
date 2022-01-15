package com.p4r4d0x.skintker.presenter.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import com.p4r4d0x.skintker.domain.usecases.GetQueriedLogsUseCase

class HomeViewModel(
    private val getLogsUseCase: GetLogsUseCase,
    private val getQueriedLogsUseCase: GetQueriedLogsUseCase
) : ViewModel() {

    companion object {
        private const val IRRITATION_LEVEL_THRESHOLD = 7
        private const val MIN_LOGS = 20
        private const val FOOD_THRESHOLD = 0.7f
        private const val ZONES_THRESHOLD = 0.7f
        private const val ALCOHOL_THRESHOLD = 0.7f
        private const val TRAVEL_THRESHOLD = 0.5f
        private val STRESS_THRESHOLDS = Pair(7, 0.5f)
        private val WEATHER_THRESHOLDS = Pair(0.5f, 0.5f)
    }

    private val _logList = MutableLiveData<List<DailyLogBO>>()
    val logList: MutableLiveData<List<DailyLogBO>>
        get() = _logList

    private val _possibleCauses = MutableLiveData<PossibleCausesBO>()
    val possibleCauses: MutableLiveData<PossibleCausesBO>
        get() = _possibleCauses

    fun getLogs() {
        getLogsUseCase.invoke(viewModelScope) {
            _logList.value = it
        }
    }

    fun getLogsByIntensityLevel() {
        getQueriedLogsUseCase.invoke(
            viewModelScope,
            params = GetQueriedLogsUseCase.Params(
                irritationLevel = IRRITATION_LEVEL_THRESHOLD,
                minLogs = MIN_LOGS,
                foodThreshold = FOOD_THRESHOLD,
                zonesThreshold = ZONES_THRESHOLD,
                travelThreshold = TRAVEL_THRESHOLD,
                alcoholThreshold = ALCOHOL_THRESHOLD,
                stressThresholds = STRESS_THRESHOLDS,
                weatherThresholds = WEATHER_THRESHOLDS
            )
        ) {
            _possibleCauses.value = it
        }
    }
}