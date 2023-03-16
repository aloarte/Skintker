package com.p4r4d0x.data.dto.logs


data class LogListResponse(val type: String, val logList: List<ReportDto>, val count: Int)