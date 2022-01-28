package com.p4r4d0x.skintker.presenter.settings.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p4r4d0x.skintker.domain.usecases.ExportLogsDBUseCase

class SettingsViewModel(
    private val addLogUseCase: ExportLogsDBUseCase
) : ViewModel() {


    fun launchExportUseCase(context: Context, resources: Resources) {
        addLogUseCase.invoke(
            scope = viewModelScope,
            params = ExportLogsDBUseCase.Params(context, resources)
        ) {

        }

    }
}