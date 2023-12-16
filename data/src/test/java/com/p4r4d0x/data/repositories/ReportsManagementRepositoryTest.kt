package com.p4r4d0x.data.repositories

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.parsers.LogsNormalizer
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testutils.TestData.LIMIT
import com.p4r4d0x.data.testutils.TestData.OFFSET
import com.p4r4d0x.data.testutils.TestData.USER_ID
import com.p4r4d0x.data.testutils.TestData.date
import com.p4r4d0x.data.testutils.TestData.log
import com.p4r4d0x.data.testutils.TestData.stringDate
import com.p4r4d0x.data.testutils.appContextModule
import com.p4r4d0x.data.testutils.databaseModule
import com.p4r4d0x.data.testutils.testDatasourcesModule
import com.p4r4d0x.data.testutils.testRepositoriesModule
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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
    KoinBaseTest(testRepositoriesModule, testDatasourcesModule, databaseModule, appContextModule) {

    private val databaseLogList = listOf(
        DailyLogDetails(
            dailyLog = fromDomain(log),
            irritation = fromDomainObject(log.irritation, 0L),
            additionalData = fromDomainObject(log.additionalData, 0L)
        )
    )

    private val logList = listOf(log)

    private val logContents = DailyLogContentsBO(count = 1, logList = logList)

    private val dao: DailyLogDao by inject()

    private val datasource: ReportsManagementDataSource by inject()

    private val normalizer: LogsNormalizer by inject()

    private lateinit var repository: ReportsManagementRepository

    @Before
    fun setUp() {
        repository = ReportsManagementRepositoryImpl(dao, datasource,normalizer)
    }

    @Test
    fun `test create report success`() {
        coEvery { normalizer.normalizeLog(log) } returns log
        coEvery { datasource.addReport(USER_ID, log) } returns
                ApiResult.Success(ReportStatus.Created)
        coEvery { dao.insertDailyLog(log) } returns true

        val logInserted = runBlocking { repository.addReport(USER_ID, log) }

        coVerify { normalizer.normalizeLog(log) }
        coVerify { datasource.addReport(USER_ID, log) }
        coVerify { dao.insertDailyLog(log) }
        Assertions.assertEquals(ReportStatus.Created, logInserted)
    }

    @Test
    fun `test update report success`() {
        coEvery { normalizer.normalizeLog(log) } returns log
        coEvery { datasource.addReport(USER_ID, log) } returns
                ApiResult.Success(ReportStatus.Edited)
        coEvery { dao.updateDailyLog(log) } returns true

        val logInserted = runBlocking { repository.editReport(USER_ID, log) }

        coVerify { normalizer.normalizeLog(log) }
        coVerify { datasource.addReport(USER_ID, log) }
        coVerify { dao.updateDailyLog(log) }
        Assertions.assertEquals(ReportStatus.Edited, logInserted)
    }

    @Test
    fun `test create report error`() {
        coEvery { normalizer.normalizeLog(log) } returns log
        coEvery { datasource.addReport(USER_ID, log) } returns
                ApiResult.Error(-1, "Error not created")

        val logInserted = runBlocking { repository.addReport(USER_ID, log) }

        coVerify { normalizer.normalizeLog(log) }
        coVerify { datasource.addReport(USER_ID, log) }
        coVerify(exactly = 0) { dao.insertDailyLog(log) }
        Assertions.assertEquals(ReportStatus.Error, logInserted)
    }

    @Test
    fun `test get report list success`() {
        coEvery { normalizer.denormalizeLog(log) } returns log
        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Success(logContents)
        coEvery { dao.insertAllDailyLogs(logList) } returns true
        coEvery { dao.getAll() } returns databaseLogList

        val logList = runBlocking { repository.getReports(USER_ID) }

        coVerify { normalizer.denormalizeLog(log) }
        coVerify { datasource.getReports(USER_ID) }
        coVerify { dao.insertAllDailyLogs(this@ReportsManagementRepositoryTest.logList) }
        coVerify { dao.getAll() }
        Assertions.assertEquals(logContents, logList)
    }

    @Test
    fun `test get report list error but had logs in database`() {
        coEvery { normalizer.denormalizeLog(log) } returns log
        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Error(
            -1,
            "Error retrieving log contents"
        )
        coEvery { dao.insertAllDailyLogs(logList) } returns false
        coEvery { dao.getAll() } returns databaseLogList

        val logList = runBlocking { repository.getReports(USER_ID) }

        coVerify { datasource.getReports(USER_ID) }
        coVerify { normalizer.denormalizeLog(log) }
        coVerify(exactly = 0) { dao.insertAllDailyLogs(this@ReportsManagementRepositoryTest.logList) }
        coVerify { dao.getAll() }
        Assertions.assertEquals(logContents, logList)
    }

    @Test
    fun `test get report list error empty database`() {
        coEvery { datasource.getReports(USER_ID) } returns ApiResult.Error(
            -1,
            "Error retrieving log contents"
        )
        coEvery { dao.insertAllDailyLogs(logList) } returns false
        coEvery { dao.getAll() } returns emptyList()

        val logList = runBlocking { repository.getReports(USER_ID) }

        coVerify { datasource.getReports(USER_ID) }
        coVerify(exactly = 0) { dao.insertAllDailyLogs(this@ReportsManagementRepositoryTest.logList) }
        coVerify { dao.getAll() }
        Assertions.assertEquals(DailyLogContentsBO(count = 0), logList)
    }

    @Test
    fun `test get report list paginated success`() {
        val logContents = DailyLogContentsBO(count = 4, logList = logList)
        coEvery { normalizer.denormalizeLog(log) } returns log
        coEvery { datasource.getReports(USER_ID, LIMIT, OFFSET) } returns ApiResult.Success(
            logContents
        )
        coEvery { dao.insertAllDailyLogs(logList) } returns true
        coEvery { dao.getLogListPaginated(LIMIT, OFFSET) } returns databaseLogList

        val logList = runBlocking { repository.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { normalizer.denormalizeLog(log) }
        coVerify { datasource.getReports(USER_ID, LIMIT, OFFSET) }
        coVerify { dao.insertAllDailyLogs(this@ReportsManagementRepositoryTest.logList) }
        coVerify { dao.getLogListPaginated(LIMIT, OFFSET) }
        Assertions.assertEquals(logContents, logList)
    }

    @Test
    fun `test get report list paginated error`() {
        coEvery { datasource.getReports(USER_ID, LIMIT, OFFSET) } returns ApiResult.Error(
            -1,
            "Error retrieving log contents"
        )
        coEvery { dao.insertAllDailyLogs(logList) } returns true
        coEvery { dao.getLogListPaginated(LIMIT, OFFSET) } returns emptyList()

        val logList = runBlocking { repository.getReports(USER_ID, LIMIT, OFFSET) }

        coVerify { datasource.getReports(USER_ID, LIMIT, OFFSET) }
        coVerify(exactly = 0) { dao.insertAllDailyLogs(this@ReportsManagementRepositoryTest.logList) }
        coVerify { dao.getLogListPaginated(LIMIT, OFFSET) }
        Assertions.assertEquals(DailyLogContentsBO(0), logList)
    }

    @Test
    fun `test delete report success`() {
        coEvery { datasource.deleteReport(USER_ID, stringDate) } returns
                ApiResult.Success(true)
        coEvery { dao.delete(date.time) } just Runs

        val logDeleted = runBlocking { repository.deleteReport(USER_ID, date) }

        coVerify { datasource.deleteReport(USER_ID, stringDate) }
        coVerify { dao.delete(date.time) }
        Assertions.assertTrue(logDeleted)
    }

    @Test
    fun `test delete report error`() {
        coEvery { datasource.deleteReport(USER_ID, stringDate) } returns
                ApiResult.Error(-1, "Error not deleted")

        val logDeleted = runBlocking { repository.deleteReport(USER_ID, date) }

        coVerify { datasource.deleteReport(USER_ID, stringDate) }
        coVerify(exactly = 0) { dao.delete(date.time) }
        Assertions.assertFalse(logDeleted)
    }

    @Test
    fun `test delete reports success`() {
        coEvery { datasource.deleteReports(USER_ID) } returns
                ApiResult.Success(true)
        coEvery { dao.deleteAll() } just Runs

        val logDeleted = runBlocking { repository.deleteReports(USER_ID) }

        coVerify { datasource.deleteReports(USER_ID) }
        coVerify { dao.deleteAll() }
        Assertions.assertTrue(logDeleted)
    }

    @Test
    fun `test delete reports error`() {
        coEvery { datasource.deleteReports(USER_ID) } returns
                ApiResult.Error(-1, "Error not deleted")

        val logDeleted = runBlocking { repository.deleteReports(USER_ID) }

        coVerify { datasource.deleteReports(USER_ID) }
        coVerify(exactly = 0) { dao.deleteAll() }
        Assertions.assertFalse(logDeleted)
    }

}