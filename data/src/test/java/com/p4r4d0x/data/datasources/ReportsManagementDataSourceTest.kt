package com.p4r4d0x.data.datasources

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.p4r4d0x.data.Constants.JSON_PARSE_EXCEPTION_CODE
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.apiModule
import com.p4r4d0x.data.datasources.impl.ReportsManagementDataSourceImpl
import com.p4r4d0x.data.dto.*
import com.p4r4d0x.data.parsers.DataParser.backendStringToDate
import com.p4r4d0x.data.parsers.toDto
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testDatasourcesModule
import com.p4r4d0x.data.testRepositoriesModule
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import com.squareup.okhttp.*
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
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
        const val USER_ID = "userId"
        const val OFFSET = 0
        const val LIMIT = 4
        const val ecInvalidDate = -2
        const val errorInvalidDate =
            "Invalid log data, the date is invalid. Follow the pattern mm-dd-yyyy"
        const val errorInvalidInput = "Invalid input. Verify that your input is not malformed"
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
        private const val JSON_ADD_ERROR = "{\n" +
                "    \"statusCode\": $ecInvalidDate,\n" +
                "    \"statusMessage\": \"$errorInvalidDate\"\n" +
                "}"
        private const val JSON_LOG_LIST = "{\n" +
                "    \"statusCode\": 0,\n" +
                "    \"content\": {\n" +
                "        \"type\": \"com.skintker.domain.model.responses.LogListResponse\",\n" +
                "        \"logList\": [\n" +
                "            {\n" +
                "                \"date\": \"09-02-2023\",\n" +
                "                \"foodList\": [\n" +
                "                    \"banana\",\n" +
                "                    \"fish\",\n" +
                "                    \"meat\"\n" +
                "                ],\n" +
                "                \"irritation\": {\n" +
                "                    \"overallValue\": 9,\n" +
                "                    \"zoneValues\": [\n" +
                "                        \"chest\",\n" +
                "                        \"ears\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"additionalData\": {\n" +
                "                    \"stressLevel\": 4,\n" +
                "                    \"weather\": {\n" +
                "                        \"humidity\": 1,\n" +
                "                        \"temperature\": 4\n" +
                "                    },\n" +
                "                    \"travel\": {\n" +
                "                        \"traveled\": false,\n" +
                "                        \"city\": \"Madrid\"\n" +
                "                    },\n" +
                "                    \"alcoholLevel\": \"Few\",\n" +
                "                    \"beerTypes\": [\n" +
                "                        \"wheat\",\n" +
                "                        \"stout\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"count\": 1\n" +
                "    }\n" +
                "}"

        private const val JSON_LOG_EMPTY_LIST = "{\n" +
                "    \"statusCode\": 0,\n" +
                "    \"content\": {\n" +
                "        \"type\": \"com.skintker.domain.model.responses.LogListResponse\",\n" +
                "        \"logList\": [],\n" +
                "        \"count\": 0\n" +
                "    }\n" +
                "}"

    }

    private val logBo = DailyLogBO(
        backendStringToDate("09-02-2023"),
        irritation = IrritationBO(9, listOf("chest", "ears")),
        additionalData = AdditionalDataBO(
            stressLevel = 4,
            weather = AdditionalDataBO.WeatherBO(humidity = 1, temperature = 4),
            travel = AdditionalDataBO.TravelBO(city = "Madrid", traveled = false),
            beerTypes = listOf("wheat", "stout"),
            alcoholLevel = AlcoholLevel.Few
        ),
        foodList = listOf("banana", "fish", "meat")
    )

    private val logDto = logBo.toDto()

    private val api: SkintkvaultApi by inject()

    private lateinit var datasource: ReportsManagementDataSource

    private var gson: Gson = Gson()


    @Before
    fun setUp() {
        datasource = ReportsManagementDataSourceImpl(api, gson)
    }

    @Test
    fun `test add report success created`() {
        coEvery { api.addReport(USER_ID, logBo.toDto()) } returns
                buildResponse(resultCode = 201, json = JSON_ADDED)

        val logInserted = runBlocking { datasource.addReport(USER_ID, logBo) }

        coVerify { api.addReport(USER_ID, logDto) }
        Assertions.assertEquals(ApiResult.Success(ReportStatus.Created), logInserted)
    }

    @Test
    fun `test add report error backend bad date`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                buildResponse(resultCode = 200, json = JSON_ADD_ERROR)

        val logInserted = runBlocking { datasource.addReport(USER_ID, logBo) }

        coVerify { api.addReport(USER_ID, logDto) }
        val expectedResult = ApiResult.Error<ReportStatus>(ecInvalidDate, errorInvalidDate)
        Assertions.assertEquals(expectedResult, logInserted)
    }

    @Test
    fun `test add report error backend bad input`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                buildResponse(resultCode = 400, message = errorInvalidInput)

        val logInserted = runBlocking { datasource.addReport(USER_ID, logBo) }

        coVerify { api.addReport(USER_ID, logDto) }
        val expected = ApiResult.Error<ReportStatus>(400, errorInvalidInput)
        Assertions.assertEquals(expected, logInserted)
    }

    @Test
    fun `test add report error json bad formed`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                buildResponse(resultCode = 200, json = JSON_BAD_FORMED)

        val logInserted = runBlocking { datasource.addReport(USER_ID, logBo) }

        coVerify { api.addReport(USER_ID, logDto) }
        val exceptionMsg =
            "java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 6 path $"
        val expected =
            ApiResult.Error<ReportStatus>(JSON_PARSE_EXCEPTION_CODE, exceptionMsg)
        Assertions.assertEquals(expected, logInserted)
    }


    @Test
    fun `test add report success update`() {
        coEvery { api.addReport(USER_ID, logDto) } returns
                buildResponse(resultCode = 200, json = JSON_EDITED)

        val logUpdated = runBlocking { datasource.addReport(USER_ID, logBo) }

        coVerify { api.addReport(USER_ID, logDto) }
        Assertions.assertEquals(ApiResult.Success(ReportStatus.Edited), logUpdated)
    }

    @Test
    fun `test get report list success`() {
        coEvery { api.getReports(USER_ID) } returns
                buildResponse(resultCode = 200, json = JSON_LOG_LIST)

        val logList = runBlocking { datasource.getReports(USER_ID) }

        coVerify { api.getReports(USER_ID) }
        val expected = ApiResult.Success(DailyLogContentsBO(count = 1, logList = listOf(logBo)))
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list success empty list`() {
        coEvery { api.getReports(USER_ID) } returns
                buildResponse(resultCode = 200, json = JSON_LOG_EMPTY_LIST)

        val logList = runBlocking { datasource.getReports(USER_ID) }

        coVerify { api.getReports(USER_ID) }
        val expected = ApiResult.Success(DailyLogContentsBO(count = 0, logList = emptyList()))
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list error`() {
        coEvery { api.getReports(USER_ID) } returns buildResponse(resultCode = 404)

        val logList = runBlocking { datasource.getReports(USER_ID) }

        coVerify { api.getReports(USER_ID) }
        val expected = ApiResult.Error<LogListResponse>(errorCode = 404, errorMessage = "")
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list paginated success`() {
        coEvery { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) } returns
                buildResponse(resultCode = 200, json = JSON_LOG_LIST)

        val logList = runBlocking { datasource.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) }
        val expected = ApiResult.Success(DailyLogContentsBO(count = 1, logList = listOf(logBo)))
        Assertions.assertEquals(expected, logList)
    }

    @Test
    fun `test get report list paginated error`() {
        coEvery {
            api.getReportsPaginated(
                USER_ID,
                LIMIT,
                OFFSET
            )
        } returns buildResponse(resultCode = 404)

        val logList = runBlocking { datasource.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { api.getReportsPaginated(USER_ID, LIMIT, OFFSET) }
        val expected = ApiResult.Error<LogListResponse>(errorCode = 404, errorMessage = "")
        Assertions.assertEquals(expected, logList)
    }

//    @Test
//    fun `test get report list success`() {
//        val logContents = DailyLogContentsBO(count = 1, logList = logList)
//        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Success(logContents)
//
//        val logInserted = runBlocking { repository.getReports(USER_ID) }
//
//        coVerify { datasource.getReports(USER_ID) }
//        Assertions.assertEquals(logContents, logInserted)
//    }
//
//    @Test
//    fun `test get report list error`() {
//        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Error(
//            -1,
//            "Error retrieving log contents"
//        )
//
//        val logInserted = runBlocking { repository.getReports(USER_ID) }
//
//        coVerify { datasource.getReports(USER_ID) }
//        Assertions.assertEquals(DailyLogContentsBO(), logInserted)
//    }
//
//    @Test
//    fun `test get report list paginated success`() {
//        val logContents = DailyLogContentsBO(count = 4, logList = logList)
//        coEvery { datasource.getReports(USER_ID, LIMIT, OFFSET) } returns ApiResult.Success(
//            logContents
//        )
//
//        val logInserted = runBlocking { repository.getReports(USER_ID, LIMIT, OFFSET) }
//
//        coVerify { datasource.getReports(USER_ID, LIMIT, OFFSET) }
//        Assertions.assertEquals(logContents, logInserted)
//    }
//
//    @Test
//    fun `test get report list paginated error`() {
//        coEvery { datasource.getReports(USER_ID, LIMIT, OFFSET) } returns ApiResult.Error(
//            -1,
//            "Error retrieving log contents"
//        )
//
//        val logInserted = runBlocking { repository.getReports(USER_ID, LIMIT, OFFSET) }
//
//        coVerify { datasource.getReports(USER_ID, LIMIT, OFFSET) }
//        Assertions.assertEquals(DailyLogContentsBO(), logInserted)
//    }
//
//    @Test
//    fun `test delete report success`() {
//        coEvery { datasource.deleteReport(USER_ID, date.toString()) } returns
//                ApiResult.Success(true)
//
//        val logDeleted = runBlocking { repository.deleteReport(USER_ID, date.toString()) }
//
//        coVerify { datasource.deleteReport(USER_ID, date.toString()) }
//        Assertions.assertTrue(logDeleted)
//    }
//
//    @Test
//    fun `test delete report error`() {
//        coEvery { datasource.deleteReport(USER_ID, date.toString()) } returns
//                ApiResult.Error(-1, "Error not deleted")
//
//        val logDeleted = runBlocking { repository.deleteReport(USER_ID, date.toString()) }
//
//        coVerify { datasource.deleteReport(USER_ID, date.toString()) }
//        Assertions.assertFalse(logDeleted)
//    }
//
//    @Test
//    fun `test delete reports success`() {
//        coEvery { datasource.deleteReports(USER_ID) } returns
//                ApiResult.Success(true)
//
//        val logDeleted = runBlocking { repository.deleteReports(USER_ID) }
//
//        coVerify { datasource.deleteReports(USER_ID) }
//        Assertions.assertTrue(logDeleted)
//    }
//
//    @Test
//    fun `test delete reports error`() {
//        coEvery { datasource.deleteReports(USER_ID) } returns
//                ApiResult.Error(-1, "Error not deleted")
//
//        val logDeleted = runBlocking { repository.deleteReports(USER_ID) }
//
//        coVerify { datasource.deleteReports(USER_ID) }
//        Assertions.assertFalse(logDeleted)
//    }


    private fun buildResponse(
        resultCode: Int,
        message: String = "",
        json: String? = ""
    ): Response = Response.Builder()
        .request(Request.Builder().url("https://some-url.com").build())
        .protocol(Protocol.HTTP_2)
        .code(resultCode)
        .message(message)
        .body(
            ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        )
        .build()

}