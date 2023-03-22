package com.p4r4d0x.data


import com.p4r4d0x.data.Constants.API_CREATED_CODE
import com.p4r4d0x.data.Constants.API_SUCCESS_CODE
import com.p4r4d0x.domain.bo.ReportStatus

fun Int.wasInsertSuccessful(): Boolean = this == API_SUCCESS_CODE || this == API_CREATED_CODE

fun Int.getInsertReportStatus(): ReportStatus = when (this) {
    API_CREATED_CODE -> ReportStatus.Created
    API_SUCCESS_CODE -> ReportStatus.Edited
    else -> ReportStatus.Error
}