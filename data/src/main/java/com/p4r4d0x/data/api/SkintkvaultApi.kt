package com.p4r4d0x.data.api

import com.p4r4d0x.data.dto.ReportDto
import com.p4r4d0x.data.dto.SkintkvaultResponse
import retrofit2.http.*


interface SkintkvaultApi {

    /* /report */

    @PUT("report/{userId}")
    suspend fun addReport(
        @Path("userId") id: String,
        @Body report: ReportDto
    ): SkintkvaultResponse

    @DELETE("report/{userId}")
    suspend fun deleteReport(
        @Path("userId") id: String,
        @Query("logDate") logDate: String
    ): SkintkvaultResponse

    /* /reports */

    @GET("reports/{userId}")
    suspend fun getReports(
        @Path("userId") id: String
    ): SkintkvaultResponse

    @GET("reports/{userId}")
    suspend fun getReportsPaginated(
        @Path("userId") id: String,
        @Query("limit") limit: Int,
        @Query("limit") offset: Int
    ): SkintkvaultResponse

    @DELETE("reports/{userId}")
    suspend fun deleteAllReports(
        @Path("userId") id: String
    ): SkintkvaultResponse

    /* /stats */

    @GET("stats/{userId}")
    suspend fun getStats(
        @Path("userId") id: String
    ): SkintkvaultResponse
}