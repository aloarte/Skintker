package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.CoroutinesTestRule
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.IrritationBO
import com.p4r4d0x.domain.di.testRepositoriesModule
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config
import java.util.*

@kotlin.time.ExperimentalTime
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class AddLogUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    companion object {
        const val USER_ID = "userId"
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val logsRepository: LogsManagementRepository by inject()

    private lateinit var useCase: AddLogUseCase

    @Before
    fun setUp() {
        useCase = AddLogUseCase(logsRepository)
    }

    @Test
    fun `test add new log`() {
        val date = Date()
        val logToInsert = DailyLogBO(
            date,
            IrritationBO(1, emptyList()),
            foodList = emptyList()
        )
        val params = AddLogUseCase.Params(USER_ID, logToInsert)

        coEvery {
            logsRepository.getLogByDate(date.time)
        } returns null
        coEvery {
            logsRepository.addDailyLog(USER_ID, logToInsert)
        } returns true

        val logAdded = runBlocking { useCase.run(params) }

        coVerify { logsRepository.getLogByDate(date.time) }
        coVerify { logsRepository.addDailyLog(USER_ID, logToInsert) }
        Assertions.assertTrue(logAdded)
    }

    @Test
    fun `test update log`() {
        val date = Date()
        val logToInsert = DailyLogBO(
            date,
            IrritationBO(1, emptyList()),
            foodList = emptyList()
        )
        val alreadyInsertedLog =
            DailyLogBO(
                date,
                IrritationBO(2, emptyList()),
                foodList = emptyList()
            )

        val params = AddLogUseCase.Params(USER_ID, logToInsert)

        coEvery {
            logsRepository.getLogByDate(date.time)
        } returns alreadyInsertedLog
        coEvery {
            logsRepository.updateDailyLog(logToInsert)
        } returns true

        val logUpdated = runBlocking { useCase.run(params) }

        coVerify { logsRepository.getLogByDate(date.time) }
        coVerify { logsRepository.updateDailyLog(logToInsert) }
        Assertions.assertTrue(logUpdated)
    }
}