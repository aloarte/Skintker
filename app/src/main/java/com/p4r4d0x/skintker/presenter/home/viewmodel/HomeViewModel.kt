package com.p4r4d0x.skintker.presenter.home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.Constants.DEFAULT_TRAVEL_THRESHOLD
import com.p4r4d0x.skintker.data.Constants.PREFERENCES_TRAVEL_THRESHOLD
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import com.p4r4d0x.skintker.domain.usecases.GetQueriedLogsUseCase
import com.p4r4d0x.skintker.domain.usecases.UpdateDdbbUseCase

class HomeViewModel(
    private val getLogsUseCase: GetLogsUseCase,
    private val getQueriedLogsUseCase: GetQueriedLogsUseCase,
    private val updateDdbbUseCase: UpdateDdbbUseCase
) : ViewModel() {

    private val _logList = MutableLiveData<List<DailyLogBO>>()
    val logList: MutableLiveData<List<DailyLogBO>>
        get() = _logList

    private val _possibleCauses = MutableLiveData<PossibleCausesBO>()
    val possibleCauses: MutableLiveData<PossibleCausesBO>
        get() = _possibleCauses

    fun getLogs() {
//        updateDdbbUseCase.invoke(viewModelScope){
        getLogsUseCase.invoke(viewModelScope) {
            _logList.value = it
        }
//        }
    }

    fun getLogsByIntensityLevel(preference: SharedPreferences?) {
        preference?.let {
            getQueriedLogsUseCase.invoke(
                viewModelScope,
                params = GetQueriedLogsUseCase.Params(
                    irritationLevel = preference.getInt(
                        Constants.PREFERENCES_IRRITATION_NUMBER,
                        Constants.DEFAULT_IRRITATION_LEVEL_THRESHOLD
                    ),
                    minLogs = preference.getInt(
                        Constants.PREFERENCES_MIN_LOGS,
                        Constants.DEFAULT_MIN_LOGS
                    ),
                    foodThreshold = preference.getFloat(
                        Constants.PREFERENCES_FOOD_THRESHOLD,
                        Constants.DEFAULT_FOOD_THRESHOLD
                    ),
                    zonesThreshold = preference.getFloat(
                        Constants.PREFERENCES_ZONES_THRESHOLD,
                        Constants.DEFAULT_ZONES_THRESHOLD
                    ),
                    travelThreshold = preference.getFloat(
                        PREFERENCES_TRAVEL_THRESHOLD,
                        DEFAULT_TRAVEL_THRESHOLD
                    ),
                    alcoholThreshold = preference.getFloat(
                        Constants.PREFERENCES_ALCOHOL_THRESHOLD,
                        Constants.DEFAULT_ALCOHOL_THRESHOLD
                    ),
                    stressThresholds = Pair(
                        preference.getInt(
                            Constants.PREFERENCES_STRESS_VALUE,
                            Constants.DEFAULT_STRESS_VALUE
                        ), preference.getFloat(
                            Constants.PREFERENCES_STRESS_THRESHOLD,
                            Constants.DEFAULT_STRESS_THRESHOLD
                        )
                    ),
                    weatherThresholds = Pair(
                        preference.getFloat(
                            Constants.PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
                            Constants.DEFAULT_WEATHER_TEMPERATURE_THRESHOLD
                        ), preference.getFloat(
                            Constants.PREFERENCES_WEATHER_HUMIDITY_THRESHOLD,
                            Constants.DEFAULT_WEATHER_HUMIDITY_THRESHOLD
                        )
                    )
                )
            ) {
                _possibleCauses.value = it
            }
        }
    }
}