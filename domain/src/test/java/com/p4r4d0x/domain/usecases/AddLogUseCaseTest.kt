package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.CoroutinesTestRule
import com.p4r4d0x.domain.TestData.date
import com.p4r4d0x.domain.TestData.log
import com.p4r4d0x.domain.bo.ReportStatus
import com.p4r4d0x.domain.di.testRepositoriesModule
import com.p4r4d0x.domain.repository.ReportsManagementRepository
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

    private val reportsRepository: ReportsManagementRepository by inject()

    private lateinit var useCase: AddLogUseCase

    @Before
    fun setUp() {
        useCase = AddLogUseCase(reportsRepository)
    }

    @Test
    fun `test add new log success`() {
        val params = AddLogUseCase.Params(USER_ID, log)
        coEvery { reportsRepository.getReport(date) } returns null
        coEvery { reportsRepository.addReport(USER_ID, log) } returns ReportStatus.Created

        val logAdded = runBlocking { useCase.run(params) }

        coVerify { reportsRepository.getReport(date) }
        coVerify { reportsRepository.addReport(USER_ID, log) }
        Assertions.assertTrue(logAdded)
    }

    @Test
    fun `test add new log failed`() {
        val params = AddLogUseCase.Params(USER_ID, log)
        coEvery { reportsRepository.getReport(date) } returns null
        coEvery { reportsRepository.addReport(USER_ID, log) } returns ReportStatus.Error

        val logAdded = runBlocking { useCase.run(params) }

        coVerify { reportsRepository.getReport(date) }
        coVerify { reportsRepository.addReport(USER_ID, log) }
        Assertions.assertFalse(logAdded)
    }

    @Test
    fun `test edit log success`() {
        val params = AddLogUseCase.Params(USER_ID, log)
        coEvery { reportsRepository.getReport(date) } returns log
        coEvery { reportsRepository.editReport(USER_ID, log) } returns ReportStatus.Edited

        val logUpdated = runBlocking { useCase.run(params) }

        coVerify { reportsRepository.getReport(date) }
        coVerify { reportsRepository.editReport(USER_ID, log) }
        Assertions.assertTrue(logUpdated)
    }

    @Test
    fun `test edit log failed`() {
        val params = AddLogUseCase.Params(USER_ID, log)
        coEvery { reportsRepository.getReport(date) } returns log
        coEvery { reportsRepository.editReport(USER_ID, log) } returns ReportStatus.Error

        val logUpdated = runBlocking { useCase.run(params) }

        coVerify { reportsRepository.getReport(date) }
        coVerify { reportsRepository.editReport(USER_ID, log) }
        Assertions.assertFalse(logUpdated)
    }
}