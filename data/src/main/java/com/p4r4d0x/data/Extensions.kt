package com.p4r4d0x.data


import com.p4r4d0x.data.Constants.API_CREATED
import com.p4r4d0x.data.Constants.API_SUCCESS
import com.p4r4d0x.domain.bo.ReportStatus

fun Int.wasInsertSuccessful(): Boolean = this == API_SUCCESS || this == API_CREATED

fun Int.getInsertReportStatus(): ReportStatus = when (this) {
    API_CREATED -> ReportStatus.Created
    API_SUCCESS -> ReportStatus.Edited
    else -> ReportStatus.Error
}