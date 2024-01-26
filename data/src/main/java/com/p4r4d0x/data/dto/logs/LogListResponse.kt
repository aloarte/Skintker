package com.p4r4d0x.data.dto.logs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogListResponse(val type: String, val logList: List<ReportDto>, val count: Int)