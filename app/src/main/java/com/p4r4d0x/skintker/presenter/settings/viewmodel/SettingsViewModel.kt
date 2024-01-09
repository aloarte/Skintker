package com.p4r4d0x.skintker.presenter.settings.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.p4r4d0x.domain.bo.ProfileBO
import com.p4r4d0x.domain.usecases.ExportLogsDBUseCase
import com.p4r4d0x.domain.usecases.RemoveLocalLogsUseCase
import com.p4r4d0x.domain.usecases.RemoveLogsUseCase
import com.p4r4d0x.domain.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel(
    private val exportLogsUseCase: ExportLogsDBUseCase,
    private val removeLocalLogsUseCase: RemoveLocalLogsUseCase,
    private val removeLogsUseCase: RemoveLogsUseCase
) : ViewModel() {

    private val _exportStatus = MutableLiveData<Boolean>()
    val exportStatus: LiveData<Boolean> = _exportStatus

    private val _removeStatus = MutableLiveData<Boolean>()
    val removeStatus: LiveData<Boolean> = _removeStatus

    private val _profile = MutableLiveData<ProfileBO>()
    val profile: LiveData<ProfileBO> = _profile

    private val _reminderTime = MutableStateFlow("")
    val reminderTime: MutableStateFlow<String>
        get() = _reminderTime

    fun launchExportUseCase(resources: Resources, context: Context, userId: String) {
        exportLogsUseCase.invoke(
            scope = viewModelScope,
            params = ExportLogsDBUseCase.Params(resources, context, userId)
        ) {
            _exportStatus.value = it
        }
    }

    fun clearLocalReports(userId: String) {
        removeLocalLogsUseCase.invoke(
            scope = viewModelScope,
            params = RemoveLocalLogsUseCase.Params(userId)
        ) {
        }
    }

    fun getLoggedUserInfo(lastSignedInAccount: GoogleSignInAccount?) {
        _profile.value = if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
            ProfileBO.AnonymousProfileBO()
        }else{
           lastSignedInAccount?.let {
                ProfileBO.AuthenticatedProfileBO(it.email ?: "", it.displayName ?: "", it.id ?: "")
            }
        }

    }

    fun updateReminderTime(prefs: SharedPreferences?) {
        val alarmHour = prefs?.getInt(Constants.PREFERENCES_ALARM_HOUR, -1) ?: -1
        val alarmMinutes = prefs?.getInt(Constants.PREFERENCES_ALARM_MINUTES, -1) ?: -1
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

    fun removeUserData(userId: String) {
        removeLogsUseCase.invoke(viewModelScope, params = RemoveLogsUseCase.Params(userId)) {
            _removeStatus.value = it
        }
    }
}