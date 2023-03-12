package com.p4r4d0x.data.dto

data class SkintkvaultResponseLogs(
    var statusCode: Int,
    var statusMessage: String? = null,
    var content: LogListResponse? = null
)

