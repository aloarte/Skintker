package com.p4r4d0x.skintker.presenter.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.usecases.GetLogsUseCase
import com.p4r4d0x.domain.usecases.GetStatsUseCase

class HomeViewModel(
    private val getLogsUseCase: GetLogsUseCase,
    private val getStatsUseCase: GetStatsUseCase
) : ViewModel() {

    private val _logList = MutableLiveData<List<DailyLogBO>>()
    val logList: MutableLiveData<List<DailyLogBO>>
        get() = _logList

    private val _possibleCauses = MutableLiveData<PossibleCausesBO>()
    val possibleCauses: MutableLiveData<PossibleCausesBO>
        get() = _possibleCauses

    fun getLogs(userId: String) {
        getLogsUseCase.invoke(viewModelScope, params = GetLogsUseCase.Params(userId)) {
            _logList.value = it
        }
    }

    fun getUserStats(userId: String) {
        getStatsUseCase.invoke(
            viewModelScope,
            params = GetStatsUseCase.Params(userId)

        ) {
            _possibleCauses.value = it
        }
    }
}