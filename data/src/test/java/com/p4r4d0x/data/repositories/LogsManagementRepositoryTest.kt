package com.p4r4d0x.data.repositories

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testDatasourcesModule
import com.p4r4d0x.data.testRepositoriesModule

import com.p4r4d0x.domain.bo.AdditionalDataBO
import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.IrritationBO
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
class LogsManagementRepositoryTest : KoinBaseTest(testRepositoriesModule, testDatasourcesModule) {

    companion object {
        const val USER_ID = "userId"
        const val CITY = "city"
        const val LOG_ID = 1L
        const val ADDITIONAL_DATA_ID = 1L
        const val IRRITATION_DATA_ID = 1L
        const val IRRITATION_LEVEL = 4
        val date = Date()
    }

    private val log = DailyLogBO(
        date,
        IrritationBO(1, emptyList()),
        foodList = emptyList()
    )
    private val logList = listOf(log)
    private val additionalDataDatabase = AdditionalData(
        additionalDataId = ADDITIONAL_DATA_ID,
        alcoholLevel = AlcoholLevel.None,
        beerType = emptyList(),
        logId = LOG_ID,
        stressLevel = 0,
        travel = AdditionalDataBO.TravelBO(city = CITY, traveled = true),
        weather = AdditionalDataBO.WeatherBO(humidity = 0, temperature = 0)
    )
    private val irritationDatabase = Irritation(
        logId = LOG_ID,
        irritationId = IRRITATION_DATA_ID,
        overallValue = 0,
        zones = emptyList()
    )
    private val logDatabase = DailyLog(
        foodList = emptyList(),
        date = date,
        id = LOG_ID
    )
    private val detailLog = DailyLogDetails(
        dailyLog = logDatabase,
        additionalData = additionalDataDatabase,
        irritation = irritationDatabase
    )
    private val logListDatabase = listOf(
        detailLog
    )
    private val logRetrievedFromDatabase = DailyLogBO(
        date,
        IrritationBO(0, emptyList()),
        AdditionalDataBO(
            stressLevel = 0,
            alcoholLevel = AlcoholLevel.None,
            weather = AdditionalDataBO.WeatherBO(
                humidity = 0,
                temperature = 0
            ),
            travel = AdditionalDataBO.TravelBO(traveled = true, city = CITY)
        ),
        foodList = emptyList()
    )
    private val logsRetrievedFromDatabase = listOf(
        logRetrievedFromDatabase
    )

    private val firebaseDatabase: FirebaseLogsManagementDataSource by inject()

    private val database: LogsDatabase by inject()

    private lateinit var repository: LogsManagementRepositoryImpl

    @Before
    fun setUp() {
        repository =
            LogsManagementRepositoryImpl(database, firebaseDatabase)
    }

    @Test
    fun `test add daily log`() {
        coEvery {
            firebaseDatabase.addSyncLog(USER_ID, log)
        } returns true
        coEvery {
            database.dailyLogDao().insertDailyLog(log)
        } returns true

        val logInserted = runBlocking {
            repository.addDailyLog(USER_ID, log)
        }

        coVerify { firebaseDatabase.addSyncLog(USER_ID, log) }
        coVerify { database.dailyLogDao().insertDailyLog(log) }
        Assertions.assertTrue(logInserted)
    }

    @Test
    fun `test add all logs`() {
        coEvery {
            database.dailyLogDao().insertAllDailyLogs(logList)
        } returns true

        val logsInserted = runBlocking {
            repository.addAllLogs(logList)
        }

        coVerify { database.dailyLogDao().insertAllDailyLogs(logList) }
        Assertions.assertTrue(logsInserted)
    }

    @Test
    fun `test update daily log`() {
        coEvery {
            database.dailyLogDao().updateDailyLog(log)
        } returns true

        val logInserted = runBlocking {
            repository.updateDailyLog(log)
        }

        coVerify { database.dailyLogDao().updateDailyLog(log) }
        Assertions.assertTrue(logInserted)
    }

    @Test
    fun `test get all logs`() {
        coEvery {
            database.dailyLogDao().getAll()
        } returns logListDatabase

        val logRetrieved = runBlocking {
            repository.getAllLogs()
        }

        coVerify { database.dailyLogDao().getAll() }
        Assertions.assertEquals(logsRetrievedFromDatabase, logRetrieved)
    }

    @Test
    fun `test get all logs with firebase`() {
        coEvery {
            firebaseDatabase.getSyncFirebaseLogs(USER_ID)
        } returns logList
        coEvery {
            database.dailyLogDao().insertAllDailyLogs(logList)
        } returns true
        coEvery {
            database.dailyLogDao().getAll()
        } returns logListDatabase

        val logRetrieved = runBlocking {
            repository.getAllLogsWithFirebase(USER_ID)
        }

        coVerify { firebaseDatabase.getSyncFirebaseLogs(USER_ID) }
        coVerify { database.dailyLogDao().insertAllDailyLogs(logList) }
        coVerify { database.dailyLogDao().getAll() }
        Assertions.assertEquals(logsRetrievedFromDatabase, logRetrieved)
    }

    @Test
    fun `test log by date`() {
        coEvery {
            database.dailyLogDao().loadLogByDate(date.time)
        } returns detailLog

        val logRetrieved = runBlocking {
            repository.getLogByDate(date.time)
        }

        coVerify { database.dailyLogDao().loadLogByDate(date.time) }
        Assertions.assertEquals(logRetrievedFromDatabase, logRetrieved)
    }

    @Test
    fun `test log by irritation level`() {
        coEvery {
            database.dailyLogDao().getLogsWithIrritationLevel(IRRITATION_LEVEL)
        } returns logListDatabase

        val logsRetrieved = runBlocking {
            repository.getLogsByIrritationLevel(IRRITATION_LEVEL)
        }

        coVerify { database.dailyLogDao().getLogsWithIrritationLevel(IRRITATION_LEVEL) }
        Assertions.assertEquals(logsRetrievedFromDatabase, logsRetrieved)
    }
}