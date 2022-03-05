package com.p4r4d0x.skintker.presenter.settings.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.p4r4d0x.skintker.domain.bo.ProfileBO
import com.p4r4d0x.skintker.domain.usecases.ExportLogsDBUseCase

class SettingsViewModel(
    private val exportLogsUseCase: ExportLogsDBUseCase
) : ViewModel() {

    private val _exportStatus = MutableLiveData<Boolean>()
    val exportStatus: LiveData<Boolean> = _exportStatus

    private val _profile = MutableLiveData<ProfileBO>()
    val profile: LiveData<ProfileBO> = _profile

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
            _profile.value = ProfileBO(it.email ?: "", it.displayName ?: "", it.id ?: "")
        }
    }
}