package com.p4r4d0x.skintker.presenter.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase

class HomeViewModel(
    private val getLogsUseCase: GetLogsUseCase
) : ViewModel() {


    private val _logList = MutableLiveData<List<DailyLogBO>>()
    val logList: MutableLiveData<List<DailyLogBO>>
        get() = _logList

    fun getLogs() {
        getLogsUseCase.invoke(viewModelScope) {
            _logList.value = it
        }
    }
}