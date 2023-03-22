package com.p4r4d0x.skintker.presenter.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.usecases.GetLogsUseCase
import com.p4r4d0x.domain.usecases.GetStatsUseCase
import com.p4r4d0x.domain.usecases.RemoveLogUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val getLogsUseCase: GetLogsUseCase,
    private val getStatsUseCase: GetStatsUseCase,
    private val removeLogUseCase: RemoveLogUseCase

) : ViewModel() {

    private val _logList = MutableLiveData<List<DailyLogBO>>()
    val logList: LiveData<List<DailyLogBO>>
        get() = _logList

    private val _possibleCauses = MutableLiveData<PossibleCausesBO>()
    val possibleCauses: LiveData<PossibleCausesBO>
        get() = _possibleCauses

    private val _logDeleted = MutableSharedFlow<Boolean>()
    val logDeleted: SharedFlow<Boolean>
        get() = _logDeleted

    fun getLogs(userId: String) {
        getLogsUseCase.invoke(viewModelScope, params = GetLogsUseCase.Params(userId)) {
            _logList.value = it
        }
    }

    fun getUserStats(userId: String) {
        getStatsUseCase.invoke(viewModelScope, params = GetStatsUseCase.Params(userId)) {
            _possibleCauses.value = it
        }
    }

    fun removeLog(userId: String, logDate: Date) {
        removeLogUseCase.invoke(viewModelScope, params = RemoveLogUseCase.Params(userId, logDate)) {
            viewModelScope.launch(Dispatchers.Main) {
                _logDeleted.emit(it)
            }
            //Update the visible log list
            if (it) {
                getLogs(userId)
            }
        }
    }
}