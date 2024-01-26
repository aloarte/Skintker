package com.p4r4d0x.data.dto

import com.p4r4d0x.data.dto.logs.LogListResponse
import com.p4r4d0x.data.dto.stats.StatsResponse
import com.p4r4d0x.data.dto.user.UserResultEnum
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SkintkvaultResponseLogs(
    @Json(name = "statusCode")
    var statusCode: Int,
    @Json(name = "statusMessage")
    var statusMessage: String? = null,
    @Json(name = "content")
    var content: LogListResponse? = null
)

@JsonClass(generateAdapter = true)
data class SkintkvaultResponseStats(
    @Json(name = "statusCode")
    var statusCode: Int,
    @Json(name = "statusMessage")
    var statusMessage: String? = null,
    @Json(name = "content")
    var content: StatsResponse? = null
)

@JsonClass(generateAdapter = true)
data class SkintkvaultResponseUser(
    @Json(name = "statusCode")
    var statusCode: Int?,
    @Json(name = "result")
    var result: UserResultEnum? = null
)