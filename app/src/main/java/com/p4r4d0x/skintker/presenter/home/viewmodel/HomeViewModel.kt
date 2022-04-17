package com.p4r4d0x.skintker.presenter.home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.domain.Constants.DEFAULT_TRAVEL_THRESHOLD
import com.p4r4d0x.domain.Constants.PREFERENCES_TRAVEL_THRESHOLD
import com.example.domain.bo.DailyLogBO
import com.example.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.usecases.GetLogsUseCase
import com.p4r4d0x.domain.usecases.GetQueriedLogsUseCase

class HomeViewModel(
    private val getLogsUseCase: GetLogsUseCase,
    private val getQueriedLogsUseCase: GetQueriedLogsUseCase
) : ViewModel() {

    private val _logList = MutableLiveData<List<com.example.domain.bo.DailyLogBO>>()
    val logList: MutableLiveData<List<com.example.domain.bo.DailyLogBO>>
        get() = _logList

    private val _possibleCauses = MutableLiveData<com.example.domain.bo.PossibleCausesBO>()
    val possibleCauses: MutableLiveData<com.example.domain.bo.PossibleCausesBO>
        get() = _possibleCauses

    fun getLogs(user: String) {
        getLogsUseCase.invoke(viewModelScope, params = GetLogsUseCase.Params(user)) {
            _logList.value = it
        }
    }

    fun getLogsByIntensityLevel(preference: SharedPreferences?) {
        preference?.let {
            getQueriedLogsUseCase.invoke(
                viewModelScope,
                params = GetQueriedLogsUseCase.Params(
                    irritationLevel = preference.getInt(
                        com.p4r4d0x.domain.Constants.PREFERENCES_IRRITATION_NUMBER,
                        com.p4r4d0x.domain.Constants.DEFAULT_IRRITATION_LEVEL_THRESHOLD
                    ),
                    minLogs = preference.getInt(
                        com.p4r4d0x.domain.Constants.PREFERENCES_MIN_LOGS,
                        com.p4r4d0x.domain.Constants.DEFAULT_MIN_LOGS
                    ),
                    foodThreshold = preference.getFloat(
                        com.p4r4d0x.domain.Constants.PREFERENCES_FOOD_THRESHOLD,
                        com.p4r4d0x.domain.Constants.DEFAULT_FOOD_THRESHOLD
                    ),
                    zonesThreshold = preference.getFloat(
                        com.p4r4d0x.domain.Constants.PREFERENCES_ZONES_THRESHOLD,
                        com.p4r4d0x.domain.Constants.DEFAULT_ZONES_THRESHOLD
                    ),
                    travelThreshold = preference.getFloat(
                        PREFERENCES_TRAVEL_THRESHOLD,
                        DEFAULT_TRAVEL_THRESHOLD
                    ),
                    alcoholThreshold = preference.getFloat(
                        com.p4r4d0x.domain.Constants.PREFERENCES_ALCOHOL_THRESHOLD,
                        com.p4r4d0x.domain.Constants.DEFAULT_ALCOHOL_THRESHOLD
                    ),
                    stressThresholds = Pair(
                        preference.getInt(
                            com.p4r4d0x.domain.Constants.PREFERENCES_STRESS_VALUE,
                            com.p4r4d0x.domain.Constants.DEFAULT_STRESS_VALUE
                        ), preference.getFloat(
                            com.p4r4d0x.domain.Constants.PREFERENCES_STRESS_THRESHOLD,
                            com.p4r4d0x.domain.Constants.DEFAULT_STRESS_THRESHOLD
                        )
                    ),
                    weatherThresholds = Pair(
                        preference.getFloat(
                            com.p4r4d0x.domain.Constants.PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
                            com.p4r4d0x.domain.Constants.DEFAULT_WEATHER_TEMPERATURE_THRESHOLD
                        ), preference.getFloat(
                            com.p4r4d0x.domain.Constants.PREFERENCES_WEATHER_HUMIDITY_THRESHOLD,
                            com.p4r4d0x.domain.Constants.DEFAULT_WEATHER_HUMIDITY_THRESHOLD
                        )
                    )
                )
            ) {
                _possibleCauses.value = it
            }
        }
    }
}