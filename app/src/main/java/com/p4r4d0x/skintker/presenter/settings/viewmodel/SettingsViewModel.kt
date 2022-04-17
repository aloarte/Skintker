package com.p4r4d0x.skintker.presenter.settings.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.example.domain.bo.ProfileBO
import com.p4r4d0x.domain.usecases.ExportLogsDBUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel(
    private val exportLogsUseCase: ExportLogsDBUseCase
) : ViewModel() {

    private val _exportStatus = MutableLiveData<Boolean>()
    val exportStatus: LiveData<Boolean> = _exportStatus

    private val _profile = MutableLiveData<com.example.domain.bo.ProfileBO>()
    val profile: LiveData<com.example.domain.bo.ProfileBO> = _profile

    private val _reminderTime = MutableStateFlow("")
    val reminderTime: MutableStateFlow<String>
        get() = _reminderTime

    fun launchExportUseCase(resources: Resources, context: Context) {
        exportLogsUseCase.invoke(
            scope = viewModelScope,
            params = ExportLogsDBUseCase.Params(resources, context)
        ) {
            _exportStatus.value = it
        }
    }

    fun getLoggedUserInfo(lastSignedInAccount: GoogleSignInAccount?) {
        lastSignedInAccount?.let {
            _profile.value =
                com.example.domain.bo.ProfileBO(it.email ?: "", it.displayName ?: "", it.id ?: "")
        }
    }

    fun updateReminderTime(prefs: SharedPreferences?) {
        val alarmHour = prefs?.getInt(com.p4r4d0x.domain.Constants.PREFERENCES_ALARM_HOUR, -1) ?: -1
        val alarmMinutes = prefs?.getInt(com.p4r4d0x.domain.Constants.PREFERENCES_ALARM_MINUTES, -1) ?: -1
        _reminderTime.value = if (alarmHour == -1 || alarmMinutes == -1) {
            ""
        } else {
            val hourStr = if (alarmHour < 10) {
                "0$alarmHour"
            } else {
                "$alarmHour"
            }
            val minutesStr = if (alarmMinutes < 10) {
                "0$alarmMinutes"
            } else {
                "$alarmMinutes"
            }
            "$hourStr:$minutesStr"
        }
    }
}