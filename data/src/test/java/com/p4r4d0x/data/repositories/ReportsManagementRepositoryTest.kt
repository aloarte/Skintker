package com.p4r4d0x.data.repositories

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testDatasourcesModule
import com.p4r4d0x.data.testRepositoriesModule
import com.p4r4d0x.domain.bo.*

import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
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
class ReportsManagementRepositoryTest :
    KoinBaseTest(testRepositoriesModule, testDatasourcesModule) {

    companion object {
        const val USER_ID = "userId"
        const val OFFSET = 0
        const val LIMIT = 4
        val date = Date()
    }

    private val log = DailyLogBO(
        date,
        irritation = IrritationBO(1, emptyList()),
        additionalData = AdditionalDataBO(
            stressLevel = 10,
            weather = AdditionalDataBO.WeatherBO(humidity = 0, temperature = 0),
            travel = AdditionalDataBO.TravelBO(city = "Madrid", traveled = false),
            beerTypes = emptyList(),
            alcoholLevel = AlcoholLevel.Few
        ),
        foodList = emptyList()
    )
    private val logList = listOf(log)

    private val datasource: ReportsManagementDataSource by inject()


    private lateinit var repository: ReportsManagementRepository

    @Before
    fun setUp() {
        repository = ReportsManagementRepositoryImpl(datasource)
    }

    @Test
    fun `test create report success`() {
        coEvery { datasource.addReport(USER_ID, log) } returns
                ApiResult.Success(ReportStatus.Created)

        val logInserted = runBlocking { repository.addReport(USER_ID, log) }

        coVerify { datasource.addReport(USER_ID, log) }
        Assertions.assertEquals(ReportStatus.Created, logInserted)
    }

    @Test
    fun `test update report success`() {
        coEvery { datasource.addReport(USER_ID, log) } returns
                ApiResult.Success(ReportStatus.Edited)

        val logInserted = runBlocking { repository.addReport(USER_ID, log) }

        coVerify { datasource.addReport(USER_ID, log) }
        Assertions.assertEquals(ReportStatus.Edited, logInserted)
    }

    @Test
    fun `test create report error`() {
        coEvery { datasource.addReport(USER_ID, log) } returns
                ApiResult.Error(-1, "Error not created")

        val logInserted = runBlocking { repository.addReport(USER_ID, log) }

        coVerify { datasource.addReport(USER_ID, log) }
        Assertions.assertEquals(ReportStatus.Error, logInserted)
    }

    @Test
    fun `test get report list success`() {
        val logContents = DailyLogContentsBO(count = 1, logList = logList)
        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Success(logContents)

        val logInserted = runBlocking { repository.getReports(USER_ID) }

        coVerify { datasource.getReports(USER_ID) }
        Assertions.assertEquals(logContents, logInserted)
    }

    @Test
    fun `test get report list error`() {
        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Error(
            -1,
            "Error retrieving log contents"
        )

        val logInserted = runBlocking { repository.getReports(USER_ID) }

        coVerify { datasource.getReports(USER_ID) }
        Assertions.assertEquals(DailyLogContentsBO(), logInserted)
    }

    @Test
    fun `test get report list paginated success`() {
        val logContents = DailyLogContentsBO(count = 4, logList = logList)
        coEvery { datasource.getReports(USER_ID, LIMIT, OFFSET) } returns ApiResult.Success(
            logContents
        )

        val logInserted = runBlocking { repository.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { datasource.getReports(USER_ID, LIMIT, OFFSET) }
        Assertions.assertEquals(logContents, logInserted)
    }

    @Test
    fun `test get report list paginated error`() {
        coEvery { datasource.getReports(USER_ID, LIMIT, OFFSET) } returns ApiResult.Error(
            -1,
            "Error retrieving log contents"
        )

        val logInserted = runBlocking { repository.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { datasource.getReports(USER_ID, LIMIT, OFFSET) }
        Assertions.assertEquals(DailyLogContentsBO(), logInserted)
    }

    @Test
    fun `test delete report success`() {
        coEvery { datasource.deleteReport(USER_ID, date.toString()) } returns
                ApiResult.Success(true)

        val logDeleted = runBlocking { repository.deleteReport(USER_ID, date.toString()) }

        coVerify { datasource.deleteReport(USER_ID, date.toString()) }
        Assertions.assertTrue(logDeleted)
    }

    @Test
    fun `test delete report error`() {
        coEvery { datasource.deleteReport(USER_ID, date.toString()) } returns
                ApiResult.Error(-1, "Error not deleted")

        val logDeleted = runBlocking { repository.deleteReport(USER_ID, date.toString()) }

        coVerify { datasource.deleteReport(USER_ID, date.toString()) }
        Assertions.assertFalse(logDeleted)
    }

    @Test
    fun `test delete reports success`() {
        coEvery { datasource.deleteReports(USER_ID) } returns
                ApiResult.Success(true)

        val logDeleted = runBlocking { repository.deleteReports(USER_ID) }

        coVerify { datasource.deleteReports(USER_ID) }
        Assertions.assertTrue(logDeleted)
    }

    @Test
    fun `test delete reports error`() {
        coEvery { datasource.deleteReports(USER_ID) } returns
                ApiResult.Error(-1, "Error not deleted")

        val logDeleted = runBlocking { repository.deleteReports(USER_ID) }

        coVerify { datasource.deleteReports(USER_ID) }
        Assertions.assertFalse(logDeleted)
    }

}