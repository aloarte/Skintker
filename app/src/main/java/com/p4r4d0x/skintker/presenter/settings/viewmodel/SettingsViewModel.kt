package com.p4r4d0x.skintker.presenter.settings.viewmodel

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.domain.usecases.ExportLogsDBUseCase
import com.p4r4d0x.skintker.domain.usecases.ImportLogsDBUseCase

class SettingsViewModel(
    private val exportLogsUseCase: ExportLogsDBUseCase,
    private val importLogsUseCase: ImportLogsDBUseCase

) : ViewModel() {


    private val _exportStatus = MutableLiveData<Boolean>()
    val exportStatus: LiveData<Boolean> = _exportStatus

    private val _importStatus = MutableLiveData<Boolean>()
    val importStatus: LiveData<Boolean> = _importStatus

    fun launchExportUseCase(context: Context, resources: Resources) {
        exportLogsUseCase.invoke(
            scope = viewModelScope,
            params = ExportLogsDBUseCase.Params(resources, context)
        ) {
            _exportStatus.value = it
        }
    }

    fun launchImportUseCase(context: Context, resources: Resources, uri: Uri?) {
        uri?.let {
            importLogsUseCase.invoke(
                scope = viewModelScope,
                params = ImportLogsDBUseCase.Params(context, resources, uri)
            ) {
                _importStatus.value = it
            }
        } ?: run {
            _importStatus.value = false
        }
    }
}