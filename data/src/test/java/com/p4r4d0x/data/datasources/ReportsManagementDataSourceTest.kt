package com.p4r4d0x.data.datasources

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.Constants.JSON_PARSE_EXCEPTION_CODE
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.impl.ReportsManagementDataSourceImpl
import com.p4r4d0x.data.dto.*
import com.p4r4d0x.data.dto.logs.LogListResponse
import com.p4r4d0x.data.dto.logs.toDto
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testutils.TestData.LIMIT
import com.p4r4d0x.data.testutils.TestData.OFFSET
import com.p4r4d0x.data.testutils.TestData.REPORT_DATE
import com.p4r4d0x.data.testutils.TestData.USER_ID
import com.p4r4d0x.data.testutils.TestData.log
import com.p4r4d0x.data.testutils.Utils.buildResponse
import com.p4r4d0x.data.testutils.apiModule
import com.p4r4d0x.data.testutils.testDatasourcesModule
import com.p4r4d0x.data.testutils.testRepositoriesModule
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import com.squareup.moshi.Moshi
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class ReportsManagementDataSourceTest :
    KoinBaseTest(testRepositoriesModule, testDatasourcesModule, apiModule) {

    companion object {
        const val ecInvalidDate = -2
        const val errorInvalidDate =
            "Invalid log data, the date is invalid. Follow the pattern mm-dd-yyyy"
        private const val JSON_BAD_FORMED = "" +
                "    \"statusCode\": 0,\n" +
                "    \"status" +
                "}"
        private const val JSON_ADDED = "{\n" +
                "    \"statusCode\": 0,\n" +
                "    \"statusMessage\": \"Report stored correctly\"\n" +
                "}"
        private const val JSON_EDITED = "{\n" +
                "        \"statusCode\": 0,\n" +
                "        \"statusMessage\": \"Report edited correctly\"\n" +
                "    }"
        const val JSON_ADD_ERROR = "{\n" +
                "    \"statusCode\": $ecInvalidDate,\n" +
                "    \"statusMessage\": \"$errorInvalidDate\"\n" +
                "}"

        const val JSON_LOG_LIST = "{\n" +
                "    \"statusCode\": 0,\n" +
                "    \"content\": {\n" +
                "        \"type\": \"com.skintker.domain.model.responses.LogListResponse\",\n" +
                "       \"count\": 1,\n" +
                "        \"logList\": [\n" +
                "          {\n" +
                "            \"date\": \"09-02-2023\",\n" +
                "            \"irritation\": {\n" +
                "              \"overallValue\": 9,\n" +
                "              \"zoneValues\": [\n" +
                "                \"chest\",\n" +
                "                \"ears\"\n" +
                "              ]\n" +
                "            },\n" +
                "            \"additionalData\": {\n" +
                "              \"stressLevel\": 4,\n" +
                "              \"weather\": {\n" +
                "                \"humidity\": 1,\n" +
                "                \"temperature\": 4\n" +
                "              },\n" +
                "              \"travel\": {\n" +
                "                \"traveled\": false,\n" +
                "                \"city\": \"Madrid\"\n" +
                "              },\n" +
                "              \"alcohol\": {\n" +
                "                \"level\": \"Beer\",\n" +
                "                \"beers\": [\n" +
                "                  \"wheat\",\n" +
                "                  \"stout\"\n" +
                "                ],\n" +
                "                \"wines\": [],\n" +
                "                \"distilledDrinks\": []\n" +
                "              }\n" +
                "            },\n" +
                "            \"foodList\": [\n" +
                "              \"banana\",\n" +
                "              \"fish\",\n" +
                "              \"meat\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]"+
                "    }\n" +
                "}"

        const val JSON_LOG_EMPTY_LIST = "{\n" +
                "    \"statusCode\": 0,\n" +
                "    \"content\": {\n" +
                "        \"type\": \"com.skintker.domain.model.responses.LogListResponse\",\n" +
                "        \"logList\": [],\n" +
                "        \"count\": 0\n" +
                "    }\n" +
                "}"

        const val JSON_DELETE_LOG = " {\n" +
                "            \"statusCode\": 0,\n" +
                "            \"statusMessage\": \"Report removed correctly\"\n" +
                "}"
        const val JSON_DELETE_LOGS = " {\n" +
                "            \"statusCode\": 0,\n" +
                "            \"statusMessage\": \"Reports removed correctly\"\n" +
                " }"
    }

    private val logDto = log.toDto()

    private val api: SkintkvaultApi by inject()

    private val mediaType: MediaType by inject()

    private lateinit var datasource: ReportsManagementDataSource

    private var moshi: Moshi = Moshi.Builder().build()

    @Before
    fun setUp() {
        datasource = ReportsManagementDataSourceImpl(api, moshi)
    }

    @Test
    fun `test add report success created`() {
        coEvery { api.addReport(USER_ID, log.toDto()) } returns
                mediaType.buildResponse(resultCode = 201, json = JSON_ADDED)

        val logInserted = runBlocking { datasource.addReport(USER_ID, log) }

        coVerify { api.addReport(USER_ID, logDto) }
        Assertions.assertEquals(ApiResult.Success(ReportStatus.Created), logInserted)
    }

    @Test
    fun `test add report error backend bad date`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_ADD_ERROR)

        val logInserted = runBlocking { datasource.addReport(USER_ID, log) }

        coVerify { api.addReport(USER_ID, logDto) }
        val expectedResult = ApiResult.Error<ReportStatus>(ecInvalidDate, errorInvalidDate)
        Assertions.assertEquals(expectedResult, logInserted)
    }

    @Test
    fun `test add report error backend bad input`() {
        coEvery { api.addReport(USER_ID, logDto) } returns mediaType.buildResponse(resultCode = 400)

        val logInserted = runBlocking { datasource.addReport(USER_ID, log) }

        coVerify { api.addReport(USER_ID, logDto) }
        val expected = ApiResult.Error<ReportStatus>(400, "Response.error()")
        Assertions.assertEquals(expected, logInserted)
    }

    @Test
    fun `test add report error json bad formed`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_BAD_FORMED)

        val logInserted = runBlocking { datasource.addReport(USER_ID, log) }

        coVerify { api.addReport(USER_ID, logDto) }
        val exceptionMsg =
            "Expected BEGIN_OBJECT but was STRING at path \$"
        val expected =
            ApiResult.Error<ReportStatus>(JSON_PARSE_EXCEPTION_CODE, exceptionMsg)
        Assertions.assertEquals(expected, logInserted)
    }


    @Test
    fun `test add report success update`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_EDITED)

        val logUpdated = runBlocking { datasource.addReport(USER_ID, log) }

        coVerify { api.addReport(USER_ID, logDto) }
        Assertions.assertEquals(ApiResult.Success(ReportStatus.Edited), logUpdated)
    }

    @Test
    fun `test get report list success`() {
        coEvery { api.getReports(USER_ID) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_LOG_LIST)

        val logList = runBlocking { datasource.getReports(USER_ID) }

        coVerify { api.getReports(USER_ID) }
        val expected = ApiResult.Success(DailyLogContentsBO(count = 1, logList = listOf(log)))
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list success empty list`() {
        coEvery { api.getReports(USER_ID) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_LOG_EMPTY_LIST)

        val logList = runBlocking { datasource.getReports(USER_ID) }

        coVerify { api.getReports(USER_ID) }
        val expected = ApiResult.Success(DailyLogContentsBO(count = 0, logList = emptyList()))
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list error`() {
        coEvery { api.getReports(USER_ID) } returns mediaType.buildResponse(resultCode = 404)

        val logList = runBlocking { datasource.getReports(USER_ID) }

        coVerify { api.getReports(USER_ID) }
        val expected =
            ApiResult.Error<LogListResponse>(errorCode = 404, errorMessage = "Response.error()")
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list paginated success`() {
        coEvery { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_LOG_LIST)

        val logList = runBlocking { datasource.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) }
        val expected = ApiResult.Success(DailyLogContentsBO(count = 1, logList = listOf(log)))
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list paginated error`() {
        coEvery { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) } returns
                mediaType.buildResponse(resultCode = 404)

        val logList = runBlocking { datasource.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) }
        val expected =
            ApiResult.Error<LogListResponse>(errorCode = 404, errorMessage = "Response.error()")
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test delete report success`() {
        coEvery { api.deleteReport(USER_ID, REPORT_DATE) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_DELETE_LOG)

        val logList = runBlocking { datasource.deleteReport(USER_ID, REPORT_DATE) }

        coVerify { api.deleteReport(USER_ID, REPORT_DATE) }
        Assertions.assertEquals(ApiResult.Success(true), logList)
    }

    @Test
    fun `test delete reports success`() {
        coEvery { api.deleteAllReports(USER_ID) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_DELETE_LOGS)

        val logList = runBlocking { datasource.deleteReports(USER_ID) }

        coVerify { api.deleteAllReports(USER_ID) }
        Assertions.assertEquals(ApiResult.Success(true), logList)
    }
}