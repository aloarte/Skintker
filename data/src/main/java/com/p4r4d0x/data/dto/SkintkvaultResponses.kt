package com.p4r4d0x.data.dto

import com.p4r4d0x.data.dto.logs.LogListResponse
import com.p4r4d0x.data.dto.stats.StatsResponse

data class SkintkvaultResponseLogs(
    var statusCode: Int,
    var statusMessage: String? = null,
    var content: LogListResponse? = null
)

data class SkintkvaultResponseStats(
    var statusCode: Int,
    var statusMessage: String? = null,
    var content: StatsResponse? = null
)